package se.payerl.noted.model.db

import androidx.room.*
import androidx.room.Dao
import androidx.sqlite.db.SimpleSQLiteQuery
import se.payerl.noted.model.NoteType
import java.time.LocalDateTime
import java.util.*

@Dao
interface NoteDAO: se.payerl.noted.model.db.Dao<NoteEntity> {
    @Query("SELECT * FROM note")
    override fun getAll(): List<NoteEntity>

    @Query("SELECT * FROM note WHERE note_name LIKE :content LIMIT 1")
    override fun findByContent(content: String): NoteEntity

    @Query("SELECT * FROM note WHERE uuid LIKE :uuid LIMIT 1")
    override fun findByUUID(uuid: String): NoteEntity?

    @Query("SELECT * FROM note WHERE parent = :uuid")
    override fun findByParent(uuid: String): List<NoteEntity>

    @Update
    override fun update(item: NoteEntity)

    @Insert
    override fun insertAll(vararg items: NoteEntity)

    @Query("INSERT INTO note (note_name, uuid, type, created_at) VALUES (:name, :uuid, :type, :createdAt)")
    fun insert(name: String, uuid: String, type: NoteType, createdAt: LocalDateTime)

    @Transaction
    override fun insert(item: NoteEntity) {
        this.insert(item.name, UUID.randomUUID().toString(), item.type, item.createdAt)
    }

    @Delete
    override fun delete(item: NoteEntity)

    @Query("SELECT CASE WHEN uuid = :uuid THEN 1 ELSE 0 END AS result FROM note WHERE uuid = :uuid")
    override fun hasUUID(uuid: String): Boolean
}