package se.payerl.noted.model.db

import androidx.room.*
import androidx.room.Dao

@Dao
interface NoteDAO: se.payerl.noted.model.db.Dao<NoteEntity> {
    @Query("SELECT * FROM note")
    override fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE note_name LIKE :content LIMIT 1")
    override fun findByContent(content: String): NoteEntity

    @Query("SELECT * FROM note WHERE uuid LIKE :uuid LIMIT 1")
    override fun findByUUID(uuid: String): NoteEntity

    @Update
    override fun update(item: NoteEntity)

    @Insert
    override fun insertAll(vararg items: NoteEntity)

    @Delete
    override fun delete(item: NoteEntity)
}