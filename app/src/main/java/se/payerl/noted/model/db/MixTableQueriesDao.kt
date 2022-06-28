package se.payerl.noted.model.db

@androidx.room.Dao
abstract class MixTableQueriesDao(val db: AppDatabase) {
    fun getChildren(uuid: String): List<Entity> {
        return listOf(
            db.noteDao().findByParent(uuid),
            db.rowTextDao().findByParent(uuid),
            db.rowAmountDao().findByParent(uuid)
        )
        .flatten()
        .sortedBy { it.createdAt }
    }
}