package com.enterprises.repair.dto

import com.enterprises.repair.entity.Technician
import com.enterprises.repair.entity.TechnicianAttendance
import com.enterprises.repair.enums.AttendanceStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class CreateTechnicianRequest(
    @field:NotBlank
    val name: String?,
    val phoneNumber: String? = null,
    val specialization: String? = null,
)

data class TechnicianResponse(
    val id: Long,
    val name: String,
    val code: String,
    val phoneNumber: String?,
    val specialization: String?,
    val active: Boolean,
    val createdAt: LocalDateTime,
)

data class AssignTechnicianRequest(
    @field:NotNull
    val technicianId: Long?,
)

data class MarkAttendanceRequest(
    val attendanceDate: LocalDate? = null,
    @field:NotNull
    val status: AttendanceStatus?,
    val checkInTime: LocalTime? = null,
    val checkOutTime: LocalTime? = null,
    val remarks: String? = null,
)

data class TechnicianAttendanceResponse(
    val id: Long,
    val technicianId: Long,
    val technicianName: String,
    val attendanceDate: LocalDate,
    val status: AttendanceStatus,
    val checkInTime: LocalTime?,
    val checkOutTime: LocalTime?,
    val remarks: String?,
    val createdAt: LocalDateTime,
)

fun Technician.toResponse() = TechnicianResponse(
    id = id,
    name = name,
    code = code,
    phoneNumber = phoneNumber,
    specialization = specialization,
    active = active,
    createdAt = createdAt,
)

fun TechnicianAttendance.toResponse() = TechnicianAttendanceResponse(
    id = id,
    technicianId = technician.id,
    technicianName = technician.name,
    attendanceDate = attendanceDate,
    status = status,
    checkInTime = checkInTime,
    checkOutTime = checkOutTime,
    remarks = remarks,
    createdAt = createdAt,
)
