package com.enterprises.repair.technician

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "technicians")
class Technician(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false, unique = true, length = 20)
    var code: String,
    var phoneNumber: String? = null,
    var specialization: String? = null,
    @Column(nullable = false)
    var active: Boolean = true,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
