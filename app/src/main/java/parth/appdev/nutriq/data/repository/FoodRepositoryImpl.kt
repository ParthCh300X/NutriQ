package parth.appdev.nutriq.data.repository

import kotlinx.coroutines.flow.Flow
import parth.appdev.nutriq.data.local.dao.FoodDao
import parth.appdev.nutriq.data.local.entity.FoodEntity
import parth.appdev.nutriq.data.remote.api.FoodApiService
import parth.appdev.nutriq.domain.model.Food
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val api: FoodApiService,
    private val dao: FoodDao
) {

    suspend fun getFood(barcode: String) =
        api.getProduct(barcode)

    suspend fun saveFood(food: Food) {
        dao.insert(
            FoodEntity(
                name = food.name,
                risk = food.riskLevel.name,
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun getHistory(): Flow<List<FoodEntity>> = dao.getAll()
}