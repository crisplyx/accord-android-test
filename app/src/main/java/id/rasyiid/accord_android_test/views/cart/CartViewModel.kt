package id.rasyiid.accord_android_test.views.cart

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.domain.product.entity.ProductEntity
import id.rasyiid.accord_android_test.domain.product.usecases.AddToCartUseCase
import id.rasyiid.accord_android_test.domain.product.usecases.GetCartsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor (
    private val getCartsUseCase: GetCartsUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _cartItemsState = MutableStateFlow<List<ProductEntity>>(emptyList())
    val cartItemsState: StateFlow<List<ProductEntity>> = _cartItemsState

    fun getCarts() {
        viewModelScope.launch {
            getCartsUseCase.invoke().collect { items ->
                _cartItemsState.value = items
            }
        }
    }
}