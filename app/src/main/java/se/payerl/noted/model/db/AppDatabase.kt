package se.payerl.noted.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import se.payerl.noted.utils.Converters

@Database(entities = [NoteEntity::class, NoteRowTextEntity::class, NoteRowAmountEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDAO
    abstract fun rowTextDao(): NoteRowTextDAO
    abstract fun rowAmountDao(): NoteRowAmountDAO
    abstract fun mixedDao(): MixTableQueriesDao
}