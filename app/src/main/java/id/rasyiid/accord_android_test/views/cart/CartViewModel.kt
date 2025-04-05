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
import id.rasyiid.accord_android_test.domain.product.usecases.RemoveCartUseCase
import id.rasyiid.accord_android_test.domain.product.usecases.UpdateCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor (
    private val getCartsUseCase: GetCartsUseCase,
    private val updateCartUseCase: UpdateCartUseCase,
    private val removeCartUseCase: RemoveCartUseCase,
    application: Application
) : AndroidViewModel(application) {

    private val _cartItemsState = MutableStateFlow<List<ProductEntity>>(emptyList())
    val cartItemsState: StateFlow<List<ProductEntity>> = _cartItemsState

    fun getItemById(productId: Int): ProductEntity? {
        return _cartItemsState.value.firstOrNull { it.id == productId }
    }

    fun getCarts() {
        viewModelScope.launch {
            getCartsUseCase.invoke().collect { items ->
                _cartItemsState.value = items
            }
        }
    }

    fun updateQuantity(productId: Int, newQuantity: Int) {
        viewModelScope.launch {
            _cartItemsState.update { currentItems ->
                currentItems.map { product ->
                    if (product.id == productId) {
                        product.copy(quantity = newQuantity)
                    } else {
                        product
                    }
                }
            }

            val product = getItemById(productId)
            product?.let {
                it.copy(quantity = newQuantity)
                updateCartUseCase.invoke(it)
            }

        }
    }

    fun removeProduct(productId: Int) {
        viewModelScope.launch {
            _cartItemsState.update { currentItems ->
                currentItems.filterNot { it.id == productId }
            }
            removeCartUseCase.invoke(productId)
        }
    }
}