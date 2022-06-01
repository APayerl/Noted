package se.payerl.noted.model

import android.util.Log
import se.payerl.noted.model.db.AppDatabase
import se.payerl.noted.model.db.NoteEntity
import se.payerl.noted.model.db.NoteRowAmountEntity
import se.payerl.noted.model.db.NoteRowTextEntity
import se.payerl.noted.utils.AddListener
import se.payerl.noted.utils.DbHelp
import se.payerl.noted.utils.ListenableList
import java.time.LocalDateTime
import java.util.*

class Note(
    override val uuid: String = UUID.randomUUID().toString(),
    override val type: NoteType = NoteType.LIST,
    override val createdAt: LocalDateTime = LocalDateTime.now()
) : NoteBase {
    var name: String = ""
    var content: ListenableList<NoteBase> = ListenableList(mutableListOf(), object : AddListener<NoteBase> {
        override fun add(element: NoteBase) {
            Log.i("Note", "Note ${element.uuid} added.")

            DbHelp.get()?.let { db ->
                when (element.type) {
                    NoteType.ROW_TEXT -> db.rowTextDao().insertAll(element.toEntity<NoteRowTextEntity>() as NoteRowTextEntity)
                    else -> db.rowAmountDao().insertAll(element.toEntity<NoteRowAmountEntity>() as NoteRowAmountEntity)
                }
            }
        }
    })

    override fun <T> toEntity(): NoteEntity {
        var entity: NoteEntity? = null
        (DbHelp.get() as AppDatabase).queryExecutor.execute {
            entity = (DbHelp.get() as AppDatabase).noteDao().findByUUID(this.uuid)
        }
        if(entity == null) entity = NoteEntity()
        entity!!.name = this.name
        return entity as NoteEntity
    }

//    override fun toEntity(): NoteEntity {
//        val entity = (DbHelp.get() as AppDatabase).noteDao().findByUUID(this.uuid)
//        entity.name = this.name
//        return entity
//    }

//    fun addNoteContent(newContent: NoteBase): Boolean {
//        return this.content.add(newContent)
//    }
//
//    fun createAndAddNoteRow(): Boolean {
//        return this.content.add(NoteRowText(this.name))
//    }
}

//data class NoteWithContentList(
//    @Embedded
//    val note: Note,
//    @Relation(
//        parentColumn = "uuid",
//        entityColumn = "uuid"
//    )
//    val contents: List<NoteBase>
//)

////@Entity
//class NoteRowText(
////    @ColumnInfo(name = "owner")
//    val owner: String,
////    @ColumnInfo(name = "content")
//    var content: String = "",
////    @ColumnInfo(name = "is_done")
//    var done: Boolean = false) : NoteBase {
////    @PrimaryKey
////    @ColumnInfo(name = "uuid")
//    override var uuid: String = UUID.randomUUID().toString()
//
////    @ColumnInfo(name = "type")
//    override var type: NoteType = NoteType.ROW_TEXT
//
////    @ColumnInfo(name = "created_at")
//    override var createdAt: LocalDateTime = LocalDateTime.now()
//}

////@Entity
//class NoteRowAmount(
////    @ColumnInfo(name = "content")
//    var content: String = "",
////    @ColumnInfo(name = "amount_done")
//    var amountDone: Int = 0,
////    @ColumnInfo(name = "amount")
//    var amount: Int = 1
//) : NoteBase {
////    @PrimaryKey
////    @ColumnInfo(name = "uuid")
//    override var uuid: String = UUID.randomUUID().toString()
//
////    @ColumnInfo(name = "type")
//    override var type: NoteType = NoteType.ROW_AMOUNT
//
////    @ColumnInfo(name = "created_at")
//    override var createdAt: LocalDateTime = LocalDateTime.now()
//}

//enum class NoteType {
//    LIST, ROW_TEXT, ROW_AMOUNT
//}

//interface NoteBase {
//    val uuid: String
//    val type: NoteType
//    val createdAt: LocalDateTime
//}

//@Dao
//interface NoteDao {
//    @Query("SELECT * FROM note")
//    fun getAll(): List<Note>
//
////    @Transaction
////    @Query("SELECT * FROM note")
////    fun getUsersWithPlaylists(): List<NoteWithContentList>
//
//    @Query("SELECT * FROM note WHERE note_name LIKE :name LIMIT 1")
//    fun findByName(name: String): Note
//
//    @Update
//    fun update(note: Note)
//
//    @Insert
//    fun insertAll(vararg users: Note)
//
//    @Delete
//    fun delete(user: Note)
//}
//
//@Database(entities = [Note::class], version = 1)
//@TypeConverters(Converters::class)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun noteDao(): NoteDao
//}