package com.enterprises.repair.service

import com.enterprises.repair.dto.RepairJobResponse
import com.enterprises.repair.dto.CreateTechnicianRequest
import com.enterprises.repair.dto.MarkAttendanceRequest
import com.enterprises.repair.entity.Technician
import com.enterprises.repair.entity.TechnicianAttendance
import com.enterprises.repair.repository.TechnicianAttendanceRepository
import com.enterprises.repair.dto.TechnicianAttendanceResponse
import com.enterprises.repair.repository.TechnicianRepository
import com.enterprises.repair.dto.TechnicianResponse
import com.enterprises.repair.dto.toResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate

@Service
class TechnicianService(
    private val technicianRepository: TechnicianRepository,
    private val technicianAttendanceRepository: TechnicianAttendanceRepository,
    private val repairJobService: RepairJobService,
) {

    @Transactional
    fun createTechnician(request: CreateTechnicianRequest): TechnicianResponse {
        val code = generateTechnicianCode()
        val technician = technicianRepository.save(
            Technician(
                name = request.name!!.trim(),
                code = code,
                phoneNumber = request.phoneNumber?.trim()?.takeIf { it.isNotEmpty() },
                specialization = request.specialization?.trim()?.takeIf { it.isNotEmpty() },
            ),
        )
        return technician.toResponse()
    }

    @Transactional(readOnly = true)
    fun getAllTechnicians(): List<TechnicianResponse> =
        technicianRepository.findAll()
            .sortedBy { it.name.lowercase() }
            .map { it.toResponse() }

    @Transactional(readOnly = true)
    fun getTechnician(id: Long): TechnicianResponse = findTechnician(id).toResponse()

    @Transactional
    fun deleteTechnician(id: Long) {
        val technician = findTechnician(id)
        technicianAttendanceRepository.deleteAllByTechnicianId(id)
        technicianRepository.delete(technician)
    }

    @Transactional
    fun assignTechnician(jobId: Long, technicianId: Long): RepairJobResponse {
        val technician = findTechnician(technicianId)
        if (!technician.active) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Technician is inactive")
        }
        return repairJobService.assignTechnician(jobId, technician.id, technician.name)
    }

    @Transactional
    fun markAttendance(technicianId: Long, request: MarkAttendanceRequest): TechnicianAttendanceResponse {
        val technician = findTechnician(technicianId)
        val attendanceDate = request.attendanceDate ?: LocalDate.now()
        val attendance = technicianAttendanceRepository.findByTechnicianIdAndAttendanceDate(technicianId, attendanceDate)
            ?.apply {
                status = requireNotNull(request.status)
                checkInTime = request.checkInTime
                checkOutTime = request.checkOutTime
                remarks = request.remarks?.trim()?.takeIf { it.isNotEmpty() }
            }
            ?: TechnicianAttendance(
                technician = technician,
                attendanceDate = attendanceDate,
                status = requireNotNull(request.status),
                checkInTime = request.checkInTime,
                checkOutTime = request.checkOutTime,
                remarks = request.remarks?.trim()?.takeIf { it.isNotEmpty() },
            )

        if (attendance.checkInTime != null && attendance.checkOutTime != null &&
            attendance.checkOutTime!!.isBefore(attendance.checkInTime)
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out time cannot be before check-in time")
        }

        return technicianAttendanceRepository.save(attendance).toResponse()
    }

    @Transactional(readOnly = true)
    fun getAttendance(technicianId: Long): List<TechnicianAttendanceResponse> {
        findTechnician(technicianId)
        return technicianAttendanceRepository.findAllByTechnicianIdOrderByAttendanceDateDesc(technicianId)
            .map { it.toResponse() }
    }

    private fun findTechnician(id: Long): Technician =
        technicianRepository.findById(id)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found for id $id") }

    private fun generateTechnicianCode(): String {
        var attempt = technicianRepository.count() + 1
        var code: String
        do {
            code = "TECH-%04d".format(attempt)
            attempt++
        } while (technicianRepository.existsByCode(code))
        return code
    }
}