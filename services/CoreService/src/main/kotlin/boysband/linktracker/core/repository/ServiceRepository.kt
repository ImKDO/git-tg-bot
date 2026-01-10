package boysband.linktracker.core.repository

import boysband.linktracker.core.model.Service
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServiceRepository : JpaRepository<Service, Long>

