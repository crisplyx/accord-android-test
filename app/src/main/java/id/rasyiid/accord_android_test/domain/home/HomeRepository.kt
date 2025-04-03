package id.rasyiid.accord_android_test.domain.home

import id.rasyiid.accord_android_test.domain.home.dto.ProductDto
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    suspend fun getProducts(token: String?): Flow<UIState<List<ProductDto>>>
}