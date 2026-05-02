package com.enterprises.repair.service

import com.enterprises.repair.dto.CreateRepairJobRequest
import com.enterprises.repair.dto.RepairJobResponse
import com.enterprises.repair.dto.UpdateRepairJobRequest
import com.enterprises.repair.dto.toResponse
import com.enterprises.repair.enums.RepairStatus
import com.enterprises.repair.enums.ServiceType
import com.enterprises.repair.entity.RepairJob
import com.enterprises.repair.repository.RepairJobRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class RepairJobService(
    private val repairJobRepository: RepairJobRepository,
) {

    @Transactional
    fun createJob(request: CreateRepairJobRequest): RepairJobResponse {
        val serviceType = requireNotNull(request.serviceType)
        validateBusinessRules(
            serviceType = serviceType,
            address = request.address,
            itemName = request.itemName,
            expectedCompletionDate = request.expectedCompletionDate,
            completedDate = request.completedDate,
        )

        val savedJob = repairJobRepository.save(
            RepairJob(
                jobNumber = generateJobNumber(serviceType),
                serviceType = serviceType,
                customerName = request.customerName!!.trim(),
                customerMobile = request.customerMobile!!.trim(),
                address = request.address?.trim()?.takeIf { it.isNotEmpty() },
                complaintDetails = request.complaintDetails!!.trim(),
                itemName = request.itemName?.trim()?.takeIf { it.isNotEmpty() },
                itemDescription = request.itemDescription?.trim()?.takeIf { it.isNotEmpty() },
                status = request.status ?: defaultStatus(request.technicianAssigned),
                technicianId = null,
                technicianAssigned = request.technicianAssigned?.trim()?.takeIf { it.isNotEmpty() },
                assignedDate = request.assignedDate,
                expectedCompletionDate = request.expectedCompletionDate,
                completedDate = request.completedDate,
            ),
        )

        return savedJob.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllJobs(): List<RepairJobResponse> = repairJobRepository.findAll()
        .sortedByDescending { it.createdAt }
        .map { it.toResponse() }

    @Transactional(readOnly = true)
    fun getJob(id: Long): RepairJobResponse = findJob(id).toResponse()

    @Transactional(readOnly = true)
    fun getJobByNumber(jobNumber: String): RepairJobResponse =
        repairJobRepository.findByJobNumber(jobNumber)?.toResponse()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Repair job not found for job number $jobNumber")

    @Transactional
    fun updateJob(id: Long, request: UpdateRepairJobRequest): RepairJobResponse {
        val job = findJob(id)

        val updatedServiceType = request.serviceType ?: job.serviceType
        val updatedAddress = request.address ?: job.address
        val updatedItemName = request.itemName ?: job.itemName
        val updatedExpectedCompletionDate = request.expectedCompletionDate ?: job.expectedCompletionDate
        val updatedCompletedDate = request.completedDate ?: job.completedDate

        validateBusinessRules(
            serviceType = updatedServiceType,
            address = updatedAddress,
            itemName = updatedItemName,
            expectedCompletionDate = updatedExpectedCompletionDate,
            completedDate = updatedCompletedDate,
        )

        request.serviceType?.let { job.serviceType = it }
        request.customerName?.let { job.customerName = it.trim() }
        request.customerMobile?.let { job.customerMobile = it.trim() }
        if (request.address != null) {
            job.address = request.address.trim().takeIf { it.isNotEmpty() }
        }
        request.complaintDetails?.let { job.complaintDetails = it.trim() }
        if (request.itemName != null) {
            job.itemName = request.itemName.trim().takeIf { it.isNotEmpty() }
        }
        if (request.itemDescription != null) {
            job.itemDescription = request.itemDescription.trim().takeIf { it.isNotEmpty() }
        }
        request.status?.let { job.status = it }
        if (request.technicianId != null) {
            job.technicianId = request.technicianId
        }
        if (request.technicianAssigned != null) {
            job.technicianAssigned = request.technicianAssigned.trim().takeIf { it.isNotEmpty() }
        }
        if (request.assignedDate != null) {
            job.assignedDate = request.assignedDate
        }
        if (request.expectedCompletionDate != null) {
            job.expectedCompletionDate = request.expectedCompletionDate
        }
        if (request.completedDate != null) {
            job.completedDate = request.completedDate
        }

        if (job.technicianAssigned != null && job.status == RepairStatus.CREATED) {
            job.status = RepairStatus.ASSIGNED
        }

        return repairJobRepository.save(job).toResponse()
    }

    @Transactional
    fun updateRepairStatus(id: Long, status: RepairStatus): RepairJobResponse {
        val job = findJob(id)
        job.status = status

        if (status == RepairStatus.ASSIGNED && job.assignedDate == null) {
            job.assignedDate = LocalDate.now()
        }

        if (status == RepairStatus.COMPLETED && job.completedDate == null) {
            job.completedDate = LocalDate.now()
        }

        return repairJobRepository.save(job).toResponse()
    }

    @Transactional
    fun assignTechnician(id: Long, technicianId: Long, technicianName: String): RepairJobResponse {
        val job = findJob(id)
        job.technicianId = technicianId
        job.technicianAssigned = technicianName
        job.assignedDate = LocalDate.now()
        if (job.status == RepairStatus.CREATED) {
            job.status = RepairStatus.ASSIGNED
        }
        return repairJobRepository.save(job).toResponse()
    }

    private fun findJob(id: Long): RepairJob =
        repairJobRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Repair job not found for id $id") }

    private fun validateBusinessRules(
        serviceType: ServiceType,
        address: String?,
        itemName: String?,
        expectedCompletionDate: LocalDate?,
        completedDate: LocalDate?,
    ) {
        when (serviceType) {
            ServiceType.HOUSE_COMPLAINT -> {
                if (address.isNullOrBlank()) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required for house complaints")
                }
            }

            ServiceType.SHOP_REPAIR -> {
                if (itemName.isNullOrBlank()) {
                    throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Item name is required for shop repairs")
                }
            }
        }

        if (completedDate != null && expectedCompletionDate != null && completedDate.isBefore(expectedCompletionDate)) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Completed date cannot be before expected completion date",
            )
        }
    }

    private fun defaultStatus(technicianAssigned: String?): RepairStatus =
        if (technicianAssigned.isNullOrBlank()) RepairStatus.CREATED else RepairStatus.ASSIGNED

    private fun generateJobNumber(serviceType: ServiceType): String {
        val today = LocalDate.now()
        val start = today.atStartOfDay()
        val end = today.plusDays(1).atStartOfDay()
        val prefix = when (serviceType) {
            ServiceType.HOUSE_COMPLAINT -> "HSE"
            ServiceType.SHOP_REPAIR -> "SHP"
        }

        var attempt = repairJobRepository.countByCreatedAtBetween(start, end) + 1
        var jobNumber: String
        do {
            jobNumber = "%s-%s-%04d".format(prefix, today.toString().replace("-", ""), attempt)
            attempt++
        } while (repairJobRepository.existsByJobNumber(jobNumber))

        return jobNumber
    }
}