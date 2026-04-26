package parth.appdev.nutriq.data.remote.dto

data class FoodResponseDto(
    val product: ProductDto?
)

data class ProductDto(
    val product_name: String?,
    val ingredients_text: String?
)