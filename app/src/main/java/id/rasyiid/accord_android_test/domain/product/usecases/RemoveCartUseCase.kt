package id.rasyiid.accord_android_test.domain.product.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.product.ProductRepository
import javax.inject.Inject

@ViewModelScoped
class RemoveCartUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(
        productId: Int
    ) = repository.deleteCartItem(productId)
}