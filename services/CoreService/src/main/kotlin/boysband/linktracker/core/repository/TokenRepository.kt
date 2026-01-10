package boysband.linktracker.core.repository

import boysband.linktracker.core.model.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository : JpaRepository<Token, Long>

