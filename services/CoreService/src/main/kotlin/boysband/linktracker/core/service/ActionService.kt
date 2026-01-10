package boysband.linktracker.core.service

import boysband.linktracker.dto.core.ActionDto
import boysband.linktracker.dto.core.CreateActionRequest
import boysband.linktracker.dto.core.UpdateActionRequest

interface ActionService {
    fun create(request: CreateActionRequest): ActionDto
    fun getById(id: Long): ActionDto
    fun getAll(): List<ActionDto>
    fun update(id: Long, request: UpdateActionRequest): ActionDto
    fun delete(id: Long)
}

