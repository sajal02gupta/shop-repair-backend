package com.enterprises.repair.repository

import com.enterprises.repair.entity.TechnicianAttendance
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface TechnicianAttendanceRepository : JpaRepository<TechnicianAttendance, Long> {
    fun findAllByTechnicianIdOrderByAttendanceDateDesc(id: Long): List<TechnicianAttendance>
    fun findByTechnicianIdAndAttendanceDate(id: Long, attendanceDate: LocalDate): TechnicianAttendance?
    fun deleteAllByTechnicianId(id: Long)
}