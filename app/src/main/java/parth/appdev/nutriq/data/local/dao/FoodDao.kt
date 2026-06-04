package parth.appdev.nutriq.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM history WHERE barcode = :barcode LIMIT 1")
    suspend fun getFoodByBarcode(barcode: String): FoodEntity?

    @Query("DELETE FROM history WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM history")
    suspend fun clearAll()
}