package se.payerl.noted.model.db

import androidx.room.*
import androidx.room.Dao

@Dao
interface NoteRowTextDAO: se.payerl.noted.model.db.Dao<NoteRowTextEntity> {
    @Query("SELECT * FROM row_text")
    override fun getAll(): List<NoteRowTextEntity>

    @Query("SELECT * FROM row_text WHERE content LIKE :content LIMIT 1")
    override fun findByContent(content: String): NoteRowTextEntity?

    @Query("SELECT * FROM row_text WHERE uuid LIKE :uuid LIMIT 1")
    override fun findByUUID(uuid: String): NoteRowTextEntity?

    @Query("SELECT * FROM row_text WHERE parent = :uuid")
    override fun findByParent(uuid: String): List<NoteRowTextEntity>

    @Update
    override fun update(item: NoteRowTextEntity)

    @Insert
    override fun insertAll(vararg items: NoteRowTextEntity)

    @Delete
    override fun delete(item: NoteRowTextEntity)

    @Query("DELETE FROM row_text WHERE uuid = :uuid")
    override fun delete(uuid: String)

    @Query("SELECT CASE WHEN uuid = :uuid THEN 1 ELSE 0 END AS result FROM row_text WHERE uuid = :uuid")
    override fun hasUUID(uuid: String): Boolean
}