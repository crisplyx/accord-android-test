package id.rasyiid.accord_android_test.domain.product.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.product.ProductRepository
import javax.inject.Inject

@ViewModelScoped
class ClearCartUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke() = repository.deleteAllCarts()
}