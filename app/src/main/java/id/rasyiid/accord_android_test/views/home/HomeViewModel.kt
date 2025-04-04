package id.rasyiid.accord_android_test.views.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.domain.product.usecases.GetProductsUseCase
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    application: Application
): AndroidViewModel(application) {
    private val _productsState = MutableStateFlow<UIState<List<ProductDto>>>(UIState.Idle)
    val productsState: StateFlow<UIState<List<ProductDto>>> = _productsState

    fun getProducts(token: String?) {
        viewModelScope.launch {
            getProductsUseCase.invoke(token).collect { result ->
                _productsState.value = result
            }
        }
    }
}