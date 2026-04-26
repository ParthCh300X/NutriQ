package parth.appdev.nutriq.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import parth.appdev.nutriq.data.local.dao.FoodDao
import parth.appdev.nutriq.data.local.db.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import parth.appdev.nutriq.data.remote.api.FoodApiService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApi(): FoodApiService {
        return Retrofit.Builder()
            .baseUrl("https://world.openfoodfacts.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FoodApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: android.content.Context
    ): AppDatabase {
        return androidx.room.Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "nutriq_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFoodDao(db: AppDatabase): FoodDao {
        return db.foodDao()
    }
}