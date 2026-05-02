package com.enterprises.repair.controller

import com.enterprises.repair.service.TechnicianService
import com.enterprises.repair.dto.CreateTechnicianRequest
import com.enterprises.repair.dto.MarkAttendanceRequest
import com.enterprises.repair.dto.TechnicianAttendanceResponse
import com.enterprises.repair.dto.TechnicianResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/technicians")
class TechnicianController(
    private val technicianService: TechnicianService,
) {

    @PostMapping
    fun createTechnician(@Valid @RequestBody request: CreateTechnicianRequest): TechnicianResponse =
        technicianService.createTechnician(request)

    @GetMapping
    fun getAllTechnicians(): List<TechnicianResponse> = technicianService.getAllTechnicians()

    @GetMapping("/{id}")
    fun getTechnician(@PathVariable id: Long): TechnicianResponse = technicianService.getTechnician(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTechnician(@PathVariable id: Long) {
        technicianService.deleteTechnician(id)
    }

    @PostMapping("/{id}/attendance")
    fun markAttendance(
        @PathVariable id: Long,
        @Valid @RequestBody request: MarkAttendanceRequest,
    ): TechnicianAttendanceResponse = technicianService.markAttendance(id, request)

    @GetMapping("/{id}/attendance")
    fun getAttendance(@PathVariable id: Long): List<TechnicianAttendanceResponse> =
        technicianService.getAttendance(id)
}