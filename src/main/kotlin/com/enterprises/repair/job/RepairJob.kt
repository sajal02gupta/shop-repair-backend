package com.enterprises.repair.job

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "repair_jobs")
class RepairJob(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true, length = 32)
    val jobNumber: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var serviceType: ServiceType,

    @Column(nullable = false)
    var customerName: String,

    @Column(name = "customer_mobile", nullable = false, length = 20)
    var customerMobile: String,

    @Column(columnDefinition = "TEXT")
    var address: String? = null,

    @Column(nullable = false, columnDefinition = "TEXT")
    var complaintDetails: String,

    var itemName: String? = null,

    @Column(columnDefinition = "TEXT")
    var itemDescription: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    var status: RepairStatus = RepairStatus.CREATED,

    var technicianId: Long? = null,

    var technicianAssigned: String? = null,

    var assignedDate: LocalDate? = null,

    var expectedCompletionDate: LocalDate? = null,

    var completedDate: LocalDate? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),
)
