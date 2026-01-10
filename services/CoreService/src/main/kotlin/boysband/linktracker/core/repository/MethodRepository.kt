package boysband.linktracker.core.repository

import boysband.linktracker.core.model.Method
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MethodRepository : JpaRepository<Method, Long>

