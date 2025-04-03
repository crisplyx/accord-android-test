package id.rasyiid.accord_android_test.domain.home.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.home.HomeRepository
import id.rasyiid.accord_android_test.domain.home.dto.ProductDto
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetProductsUseCase @Inject constructor (private val repository: HomeRepository) {
    suspend fun execute(token: String?): Flow<UIState<List<ProductDto>>> = repository.getProducts(token)
}