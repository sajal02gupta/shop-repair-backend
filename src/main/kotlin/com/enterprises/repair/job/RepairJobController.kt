package com.enterprises.repair.job

import jakarta.validation.Valid
import com.enterprises.repair.technician.AssignTechnicianRequest
import com.enterprises.repair.technician.TechnicianService
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

    @PostMapping("/{id}/assign-technician")
    fun assignTechnician(
        @PathVariable id: Long,
        @Valid @RequestBody request: AssignTechnicianRequest,
    ): RepairJobResponse = technicianService.assignTechnician(id, request.technicianId!!)
}
