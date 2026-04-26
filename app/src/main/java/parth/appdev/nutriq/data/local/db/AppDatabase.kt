package parth.appdev.nutriq.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import parth.appdev.nutriq.data.local.dao.FoodDao
import parth.appdev.nutriq.data.local.entity.FoodEntity

@Database(
    entities = [FoodEntity::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
}