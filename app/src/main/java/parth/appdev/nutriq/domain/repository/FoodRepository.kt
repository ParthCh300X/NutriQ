package parth.appdev.nutriq.domain.repository

import kotlinx.coroutines.flow.Flow
import parth.appdev.nutriq.data.local.entity.FoodEntity
import parth.appdev.nutriq.domain.model.Food
import retrofit2.Response
import parth.appdev.nutriq.data.remote.dto.FoodResponseDto

interface FoodRepository {
    suspend fun getFood(barcode: String): Response<FoodResponseDto>
    suspend fun saveFood(food: Food)
    fun getHistory(): Flow<List<FoodEntity>>
}