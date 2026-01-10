package boysband.linktracker.core.controller

import boysband.linktracker.core.service.HistoryAnswerService
import boysband.linktracker.dto.core.CreateHistoryAnswerRequest
import boysband.linktracker.dto.core.HistoryAnswerDto
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api/history-answers")
class HistoryAnswerController(
    private val service: HistoryAnswerService
) {

    @PostMapping
    fun create(@Validated @RequestBody request: CreateHistoryAnswerRequest): ResponseEntity<HistoryAnswerDto> {
        val dto = service.create(request)
        return ResponseEntity.created(URI.create("/api/history-answers/${dto.id}")).body(dto)
    }

    @GetMapping
    fun getAll(): List<HistoryAnswerDto> = service.getAll()

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): HistoryAnswerDto = service.getById(id)

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        service.delete(id)
        return ResponseEntity.noContent().build()
    }
}

