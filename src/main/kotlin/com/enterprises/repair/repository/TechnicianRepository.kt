package com.enterprises.repair.repository

import com.enterprises.repair.entity.Technician
import org.springframework.data.jpa.repository.JpaRepository

interface TechnicianRepository : JpaRepository<Technician, Long> {
    fun existsByCode(code: String): Boolean
}