package boysband.linktracker.core.controller

import boysband.linktracker.core.service.MethodService
import boysband.linktracker.dto.core.CreateMethodRequest
import boysband.linktracker.dto.core.UpdateMethodRequest
import boysband.linktracker.dto.core.MethodDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/methods")
class MethodController(
    private val service: MethodService
) {

    @PostMapping
    fun create(@RequestBody request: CreateMethodRequest): ResponseEntity<MethodDto> {
        val dto = service.create(request)
        return ResponseEntity.created(URI.create("/api/methods/${dto.id}")).body(dto)
    }

    @GetMapping
    fun getAll(): List<MethodDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): MethodDto = service.getById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UpdateMethodRequest): MethodDto = service.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}
