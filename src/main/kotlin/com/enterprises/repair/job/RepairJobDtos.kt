package com.enterprises.repair.job

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate
import java.time.LocalDateTime

data class CreateRepairJobRequest(
    @field:NotNull
    var serviceType: ServiceType?,
    @field:NotBlank
    @field:Size(max = 255)
    val customerName: String?,
    var customerMobile: String?,
    val address: String? = null,
    @field:NotBlank
    val complaintDetails: String?,
    val itemName: String? = null,
    val itemDescription: String? = null,
    val status: RepairStatus? = null,
    val technicianAssigned: String? = null,
    val assignedDate: LocalDate? = null,
    val expectedCompletionDate: LocalDate? = null,
    val completedDate: LocalDate? = null,
)

data class UpdateRepairJobRequest(
    val serviceType: ServiceType? = null,
    val customerName: String? = null,
    val customerMobile: String? = null,
    val address: String? = null,
    val complaintDetails: String? = null,
    val itemName: String? = null,
    val itemDescription: String? = null,
    val status: RepairStatus? = null,
    val technicianId: Long? = null,
    val technicianAssigned: String? = null,
    val assignedDate: LocalDate? = null,
    val expectedCompletionDate: LocalDate? = null,
    val completedDate: LocalDate? = null,
)

data class RepairJobResponse(
    val id: Long,
    val jobNumber: String,
    val serviceType: ServiceType,
    val customerName: String,
    val customerMobile: String,
    val address: String?,
    val complaintDetails: String,
    val itemName: String?,
    val itemDescription: String?,
    val status: RepairStatus,
    val technicianId: Long?,
    val technicianAssigned: String?,
    val assignedDate: LocalDate?,
    val expectedCompletionDate: LocalDate?,
    val completedDate: LocalDate?,
    val createdAt: LocalDateTime,
)

fun RepairJob.toResponse() = RepairJobResponse(
    id = id,
    jobNumber = jobNumber,
    serviceType = serviceType,
    customerName = customerName,
    customerMobile = customerMobile,
    address = address,
    complaintDetails = complaintDetails,
    itemName = itemName,
    itemDescription = itemDescription,
    status = status,
    technicianId = technicianId,
    technicianAssigned = technicianAssigned,
    assignedDate = assignedDate,
    expectedCompletionDate = expectedCompletionDate,
    completedDate = completedDate,
    createdAt = createdAt,
)
