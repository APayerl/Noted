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
    override fun findByUUID(uuid: String): NoteEntity?

    @Query("SELECT * FROM note WHERE parent = :uuid")
    override fun findByParent(uuid: String): List<NoteEntity>

    @Query("SELECT * FROM note WHERE parent IS NULL ORDER BY created_at")
    fun findRootLists(): List<NoteEntity>

    @Update
    override fun update(item: NoteEntity)

    @Insert
    override fun insertAll(vararg items: NoteEntity)

    @Delete
    override fun delete(item: NoteEntity)

    @Query("DELETE FROM note WHERE uuid = :uuid")
    override fun delete(uuid: String)

    @Query("SELECT CASE WHEN uuid = :uuid THEN 1 ELSE 0 END AS result FROM note WHERE uuid = :uuid")
    override fun hasUUID(uuid: String): Boolean
}