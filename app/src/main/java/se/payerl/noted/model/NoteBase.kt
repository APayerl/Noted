package se.payerl.noted.model

import java.time.LocalDateTime

interface NoteBase {
    val uuid: String
    val type: NoteType
    val createdAt: LocalDateTime
}