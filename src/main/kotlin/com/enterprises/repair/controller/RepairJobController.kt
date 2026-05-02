package com.enterprises.repair.controller

import com.enterprises.repair.dto.CreateRepairJobRequest
import com.enterprises.repair.dto.RepairJobResponse
import com.enterprises.repair.service.RepairJobService
import com.enterprises.repair.dto.UpdateRepairJobRequest
import com.enterprises.repair.dto.UpdateRepairStatusRequest
import com.enterprises.repair.dto.AssignTechnicianRequest
import com.enterprises.repair.service.TechnicianService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/jobs")
class RepairJobController(
    private val repairJobService: RepairJobService,
    private val technicianService: TechnicianService,
) {

    @PostMapping
    fun createJob(@Valid @RequestBody request: CreateRepairJobRequest): RepairJobResponse =
        repairJobService.createJob(request)

    @GetMapping
    fun getAllJobs(): List<RepairJobResponse> = repairJobService.getAllJobs()

    @GetMapping("/{id}")
    fun getJob(@PathVariable id: Long): RepairJobResponse = repairJobService.getJob(id)

    @GetMapping("/job-number/{jobNumber}")
    fun getJobByJobNumber(@PathVariable jobNumber: String): RepairJobResponse =
        repairJobService.getJobByNumber(jobNumber)

    @PatchMapping("/{id}")
    fun updateJob(
        @PathVariable id: Long,
        @RequestBody request: UpdateRepairJobRequest,
    ): RepairJobResponse = repairJobService.updateJob(id, request)

    @PatchMapping("/{id}/update-repair-status")
    fun updateRepairStatus(
        @PathVariable id: Long,
        @Valid @RequestBody request: UpdateRepairStatusRequest,
    ): RepairJobResponse = repairJobService.updateRepairStatus(id, request.status!!)

    @PostMapping("/{id}/assign-technician")
    fun assignTechnician(
        @PathVariable id: Long,
        @Valid @RequestBody request: AssignTechnicianRequest,
    ): RepairJobResponse = technicianService.assignTechnician(id, request.technicianId!!)
}