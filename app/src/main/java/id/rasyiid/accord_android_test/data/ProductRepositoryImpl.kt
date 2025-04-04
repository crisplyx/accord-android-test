package id.rasyiid.accord_android_test.data

import id.rasyiid.accord_android_test.BuildConfig.BASE_URL
import id.rasyiid.accord_android_test.domain.product.CartDao
import id.rasyiid.accord_android_test.domain.product.ProductRepository
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import id.rasyiid.accord_android_test.views.UIState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor (private val httpClient: HttpClient, private val cartDao: CartDao): ProductRepository {
    override suspend fun getProducts(token: String?): Flow<UIState<List<ProductDto>>> {
        return flow<UIState<List<ProductDto>>> {
            emit(UIState.Loading)
            try {
                val url = "${BASE_URL}/products"
                val response: HttpResponse = httpClient.request {
                    method = HttpMethod.Get
                    url(url)
                    token?.let {
                        headers {
                            append("Authorization", "Bearer $it")
                        }
                    }
                }
                if(response.status == HttpStatusCode.OK) {
                    emit(UIState.Success(response.body<List<ProductDto>>()))
                } else {
                    emit(UIState.Error("Error"))
                }
            } catch (error: Exception) {
                emit(UIState.Error(error.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getProductDetails(
        token: String?,
        productId: Int
    ): Flow<UIState<ProductDto>> {
        return flow<UIState<ProductDto>> {
            emit(UIState.Loading)
            try {
                val url = "${BASE_URL}/products/${productId}"
                val response: HttpResponse = httpClient.request {
                    method = HttpMethod.Get
                    url(url)
                    token?.let {
                        headers {
                            append("Authorization", "Bearer $it")
                        }
                    }
                }
                if(response.status == HttpStatusCode.OK) {
                    emit(UIState.Success(response.body<ProductDto>()))
                } else {
                    emit(UIState.Error("Error"))
                }
            } catch (error: Exception) {
                emit(UIState.Error(error.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun insertToCart(productEntity: ProductEntity) {
        cartDao.insertCartItem(productEntity)
    }

    override suspend fun updateCartItem(productEntity: ProductEntity) {
        cartDao.updateCartItem(productEntity)
    }

    override suspend fun deleteCartItem(productId: Int) {
        cartDao.deleteCartItem(productId)
    }

    override suspend fun deleteAllCarts() {
        cartDao.deleteAllCarts()
    }

    override fun getCarts(): Flow<List<ProductEntity>> {
        return cartDao.getCarts()
    }
}