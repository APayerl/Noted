package se.payerl.noted.utils

import androidx.room.TypeConverter
import se.payerl.noted.model.db.Entity
import se.payerl.noted.model.db.NoteEntity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.Temporal

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): LocalDateTime {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
    }

    @TypeConverter
    fun toTimestamp(value: LocalDateTime): Long {
        return value.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

    @TypeConverter
    fun fromNoteEntity(value: NoteEntity): Entity {
        return value
    }

    @TypeConverter
    fun toNoteEntity(value: Entity): NoteEntity {
        return value as NoteEntity
    }
}