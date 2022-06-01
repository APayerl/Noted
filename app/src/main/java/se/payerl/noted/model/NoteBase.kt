package se.payerl.noted.model

import se.payerl.noted.model.db.Entity
import java.time.LocalDateTime

interface NoteBase {
    val uuid: String
    val type: NoteType
    val createdAt: LocalDateTime

    fun <T> toEntity(): Entity
}