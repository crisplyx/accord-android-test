package id.rasyiid.accord_android_test.views.product

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import id.rasyiid.accord_android_test.domain.product.usecases.AddToCartUseCase
import id.rasyiid.accord_android_test.domain.product.usecases.GetProductDetailsUseCase
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val getProductDetailsUseCase: GetProductDetailsUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    application: Application
): AndroidViewModel(application) {

    fun addCartItem(productEntity: ProductEntity) = viewModelScope.launch {
        addToCartUseCase.invoke(productEntity)
    }

    private val _productDetailState = MutableStateFlow<UIState<ProductDto>>(UIState.Idle)
    val productDetailState: StateFlow<UIState<ProductDto>> = _productDetailState

    fun getProductDetail(token: String?, productId: Int) {
        viewModelScope.launch {
            getProductDetailsUseCase.invoke(token, productId).collect { result ->
                _productDetailState.value = result
            }
        }
    }
}