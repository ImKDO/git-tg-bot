package boysband.linktracker.core.service

import boysband.linktracker.dto.core.NewService
import boysband.linktracker.dto.core.CreateServiceRequest
import boysband.linktracker.dto.core.UpdateServiceRequest

interface ServiceService {
    fun create(request: CreateServiceRequest): NewService
    fun getById(id: Long): NewService
    fun getAll(): List<NewService>
    fun update(id: Long, request: UpdateServiceRequest): NewService
    fun delete(id: Long)
}

