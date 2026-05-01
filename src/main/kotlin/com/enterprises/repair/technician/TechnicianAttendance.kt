package com.enterprises.repair.technician

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "technician_attendance")
class TechnicianAttendance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "technician_id", nullable = false)
    val technician: Technician,
    @Column(nullable = false)
    val attendanceDate: LocalDate,
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: AttendanceStatus,
    var checkInTime: LocalTime? = null,
    var checkOutTime: LocalTime? = null,
    @Column(columnDefinition = "TEXT")
    var remarks: String? = null,
    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
