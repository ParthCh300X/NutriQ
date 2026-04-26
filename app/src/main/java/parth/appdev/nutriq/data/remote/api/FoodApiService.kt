package parth.appdev.nutriq.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import parth.appdev.nutriq.data.remote.dto.FoodResponseDto

interface FoodApiService {

    @GET("api/v0/product/{barcode}.json")
    suspend fun getProduct(
        @Path("barcode") barcode: String
    ): Response<FoodResponseDto>
}