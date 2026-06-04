package parth.appdev.nutriq.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "history",
    indices = [Index(value = ["barcode"], unique = true)]
)
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val barcode: String,
    val name: String,
    val risk: String,
    val ingredients: String,
    val timestamp: Long
)