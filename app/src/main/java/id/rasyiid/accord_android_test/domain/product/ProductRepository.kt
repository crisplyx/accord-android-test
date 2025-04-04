package id.rasyiid.accord_android_test.domain.product

import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProducts(token: String?): Flow<UIState<List<ProductDto>>>
    suspend fun getProductDetails(token: String?, productId: Int): Flow<UIState<ProductDto>>

    suspend fun insertToCart(productEntity: ProductEntity)
    suspend fun updateCartItem(productEntity: ProductEntity)
    suspend fun deleteCartItem(productId: Int)
    suspend fun deleteAllCarts()

    fun getCarts(): Flow<List<ProductEntity>>
}