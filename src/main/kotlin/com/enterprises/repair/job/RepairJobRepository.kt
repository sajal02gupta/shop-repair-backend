package com.enterprises.repair.job

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface RepairJobRepository : JpaRepository<RepairJob, Long> {
    fun existsByJobNumber(jobNumber: String): Boolean
    fun findByJobNumber(jobNumber: String): RepairJob?
    fun countByCreatedAtBetween(start: LocalDateTime, end: LocalDateTime): Long
}
