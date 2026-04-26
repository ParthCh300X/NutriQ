package parth.appdev.nutriq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import parth.appdev.nutriq.data.local.entity.FoodEntity

@Dao
interface FoodDao {

    @Insert
    suspend fun insert(food: FoodEntity)

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<FoodEntity>>
}