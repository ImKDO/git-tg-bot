package boysband.linktracker.core.service

import boysband.linktracker.core.repository.NewServicesRepository
import boysband.linktracker.dto.core.NewService
import org.springframework.stereotype.Service

@Service
class NewServicesService(
    repository : NewServicesRepository
) {

    fun addService(
        newService: NewService
    ) {

    }

}