package id.rasyiid.accord_android_test.domain.product

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(productEntity: ProductEntity)

    @Update
    suspend fun updateCartItem(productEntity: ProductEntity)

    @Query("DELETE FROM carts WHERE id= :productId")
    suspend fun deleteCartItem(productId: Int): Int

    @Query("SELECT * FROM carts")
    fun getCarts(): Flow<List<ProductEntity>>

    @Query("DELETE FROM carts")
    suspend fun deleteAllCarts(): Int
}