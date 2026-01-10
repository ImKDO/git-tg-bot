package boysband.linktracker.core.controller

import boysband.linktracker.core.service.ActionService
import boysband.linktracker.dto.core.ActionDto
import boysband.linktracker.dto.core.CreateActionRequest
import boysband.linktracker.dto.core.UpdateActionRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/actions")
class ActionController(
    private val service: ActionService
) {

    @PostMapping
    fun create(@RequestBody request: CreateActionRequest): ResponseEntity<ActionDto> {
        val dto = service.create(request)
        return ResponseEntity.created(URI.create("/api/actions/${dto.id}")).body(dto)
    }

    @GetMapping
    fun getAll(): List<ActionDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ActionDto = service.getById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UpdateActionRequest): ActionDto = service.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}

