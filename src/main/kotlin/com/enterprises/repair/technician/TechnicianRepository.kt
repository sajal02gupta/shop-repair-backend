package com.enterprises.repair.technician

import org.springframework.data.jpa.repository.JpaRepository

interface TechnicianRepository : JpaRepository<Technician, Long> {
    fun existsByCode(code: String): Boolean
}
