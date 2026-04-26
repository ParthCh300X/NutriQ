package parth.appdev.nutriq.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import parth.appdev.nutriq.data.local.entity.FoodEntity

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity)

    @Update
    suspend fun update(food: FoodEntity)

    @Query("SELECT * FROM history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<FoodEntity>>

    @Query("SELECT * FROM history WHERE name = :name LIMIT 1")
    suspend fun getFoodByName(name: String): FoodEntity?
}