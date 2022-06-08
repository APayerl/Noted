package se.payerl.noted.model

import java.io.Serializable
import java.time.LocalDateTime

interface NoteBase : Serializable {
    val uuid: String
    val type: NoteType
    val createdAt: LocalDateTime
}