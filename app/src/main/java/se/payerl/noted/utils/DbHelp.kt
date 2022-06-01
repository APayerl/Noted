package se.payerl.noted.utils

import android.content.Context
import androidx.room.CoroutinesRoom
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import se.payerl.noted.model.db.AppDatabase

object DbHelp {
    private var _instance: AppDatabase? = null
    fun createInstance(applicationContext: Context): AppDatabase {
        if(_instance == null) {
            _instance = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "noted-db").build()
        }
        return _instance as AppDatabase
    }

    fun get(): AppDatabase? {
        return _instance
    }
}