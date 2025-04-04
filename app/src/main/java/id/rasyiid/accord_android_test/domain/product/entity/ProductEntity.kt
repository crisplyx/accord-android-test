package id.rasyiid.accord_android_test.domain.product.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carts")
data class ProductEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val quantity: Int
)
