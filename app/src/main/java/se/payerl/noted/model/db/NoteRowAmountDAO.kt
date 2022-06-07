package se.payerl.noted.model.db

import androidx.room.*
import androidx.room.Dao

@Dao
interface NoteRowAmountDAO: se.payerl.noted.model.db.Dao<NoteRowAmountEntity> {
    @Query("SELECT * FROM row_amount")
    override fun getAll(): List<NoteRowAmountEntity>

    @Query("SELECT * FROM row_amount WHERE content LIKE :content LIMIT 1")
    override fun findByContent(content: String): NoteRowAmountEntity

    @Query("SELECT * FROM row_amount WHERE uuid LIKE :uuid LIMIT 1")
    override fun findByUUID(uuid: String): NoteRowAmountEntity

    @Update
    override fun update(item: NoteRowAmountEntity)

    @Insert
    override fun insertAll(vararg items: NoteRowAmountEntity)

    @Insert
    override fun insert(item: NoteRowAmountEntity)

    @Delete
    override fun delete(item: NoteRowAmountEntity)

    @Query("SELECT CASE WHEN uuid = :uuid THEN 1 ELSE 0 END AS result FROM row_amount WHERE uuid = :uuid")
    override fun hasUUID(uuid: String): Boolean
}