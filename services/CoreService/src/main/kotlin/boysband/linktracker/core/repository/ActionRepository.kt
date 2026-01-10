package boysband.linktracker.core.repository

import boysband.linktracker.core.model.Action
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ActionRepository : JpaRepository<Action, Long>

