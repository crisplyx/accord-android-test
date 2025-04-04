package id.rasyiid.accord_android_test.domain.product.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.product.ProductRepository
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetProductsUseCase @Inject constructor (private val repository: ProductRepository) {
    suspend fun invoke(token: String?): Flow<UIState<List<ProductDto>>> = repository.getProducts(token)
}