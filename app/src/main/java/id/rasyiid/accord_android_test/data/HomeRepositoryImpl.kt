package id.rasyiid.accord_android_test.data

import id.rasyiid.accord_android_test.BuildConfig.BASE_URL
import id.rasyiid.accord_android_test.domain.home.HomeRepository
import id.rasyiid.accord_android_test.domain.home.dto.ProductDto
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

class HomeRepositoryImpl @Inject constructor (private val httpClient: HttpClient): HomeRepository {
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
}