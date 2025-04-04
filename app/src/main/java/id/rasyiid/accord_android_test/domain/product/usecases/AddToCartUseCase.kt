package id.rasyiid.accord_android_test.domain.product.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.product.ProductRepository
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import javax.inject.Inject

@ViewModelScoped
class AddToCartUseCase @Inject constructor(private val repository: ProductRepository) {
    suspend operator fun invoke(
        productEntity: ProductEntity
    ) = repository.insertToCart(productEntity)
}