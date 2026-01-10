package boysband.linktracker.core.service

import boysband.linktracker.dto.core.MethodDto
import boysband.linktracker.dto.core.CreateMethodRequest
import boysband.linktracker.dto.core.UpdateMethodRequest

interface MethodService {
    fun create(request: CreateMethodRequest): MethodDto
    fun getById(id: Long): MethodDto
    fun getAll(): List<MethodDto>
    fun update(id: Long, request: UpdateMethodRequest): MethodDto
    fun delete(id: Long)
}
