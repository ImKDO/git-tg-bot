package boysband.linktracker.core.repository

import boysband.linktracker.core.service.NewServicesService
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NewServicesRepository: JpaRepository<NewServicesService, Integer> {
}