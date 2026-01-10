package boysband.linktracker.core.controller

import boysband.linktracker.core.service.ServiceService
import boysband.linktracker.dto.core.CreateServiceRequest
import boysband.linktracker.dto.core.UpdateServiceRequest
import boysband.linktracker.dto.core.NewService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/services")
class ServiceController(
    private val service: ServiceService
) {

    @PostMapping
    fun create(@RequestBody request: CreateServiceRequest): ResponseEntity<NewService> {
        val dto = service.create(request)
        return ResponseEntity.created(URI.create("/api/services/${dto.link}")).body(dto)
    }

    @GetMapping
    fun getAll(): List<NewService> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): NewService = service.getById(id)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: UpdateServiceRequest): NewService = service.update(id, request)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}

