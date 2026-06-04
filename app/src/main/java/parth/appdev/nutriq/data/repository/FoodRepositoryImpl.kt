package parth.appdev.nutriq.data.repository

import kotlinx.coroutines.flow.Flow
import parth.appdev.nutriq.data.local.dao.FoodDao
import parth.appdev.nutriq.data.local.entity.FoodEntity
import parth.appdev.nutriq.data.remote.api.FoodApiService
import parth.appdev.nutriq.data.remote.dto.FoodResponseDto
import parth.appdev.nutriq.domain.model.Food
import parth.appdev.nutriq.domain.repository.FoodRepository
import retrofit2.Response
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val api: FoodApiService,
    private val dao: FoodDao
) : FoodRepository {

    override suspend fun getFood(barcode: String): Response<FoodResponseDto> =
        api.getProduct(barcode)

    override suspend fun saveFood(food: Food) {
        val existing = dao.getFoodByBarcode(food.barcode)

        val entity = FoodEntity(
            barcode = food.barcode,
            name = food.name,
            risk = food.riskLevel.name,
            ingredients = food.ingredients,
            timestamp = System.currentTimeMillis()
        )

        if (existing != null) {
            dao.update(entity.copy(id = existing.id))
        } else {
            dao.insert(entity)
        }
    }

    override fun getHistory(): Flow<List<FoodEntity>> = dao.getAll()
}