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

        val existing = dao.getFoodByName(food.name)

        val entity = FoodEntity(
            name = food.name,
            risk = food.riskLevel.name,
            ingredients = food.ingredients,
            timestamp = System.currentTimeMillis()
        )

        if (existing != null) {
            // 🔁 Update existing (keeps list clean)
            dao.update(entity.copy(id = existing.id))
        } else {
            dao.insert(entity)
        }
    }

    fun getHistory(): Flow<List<FoodEntity>> = dao.getAll()
}