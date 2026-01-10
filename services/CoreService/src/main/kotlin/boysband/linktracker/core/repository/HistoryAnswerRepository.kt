package boysband.linktracker.core.repository

import boysband.linktracker.core.model.HistoryAnswer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface HistoryAnswerRepository : JpaRepository<HistoryAnswer, Long>
