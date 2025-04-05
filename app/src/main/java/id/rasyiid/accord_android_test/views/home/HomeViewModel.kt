package id.rasyiid.accord_android_test.views.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rasyiid.accord_android_test.domain.product.dto.ProductDto
import id.rasyiid.accord_android_test.domain.product.usecases.GetProductsUseCase
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    application: Application
): AndroidViewModel(application) {
    private val _productsFilteredState = MutableStateFlow<UIState<List<ProductDto>>>(UIState.Idle)
    val productsFilteredState: StateFlow<UIState<List<ProductDto>>> = _productsFilteredState

    private val _productsState = MutableStateFlow<UIState<List<ProductDto>>>(UIState.Idle)
    val productsState: StateFlow<UIState<List<ProductDto>>> = _productsState

    private var unfilteredProducts: MutableList<ProductDto> = mutableListOf()

    fun getProducts(token: String?) {
        viewModelScope.launch {
            getProductsUseCase.invoke(token).collect { result ->
                _productsState.value = result
                _productsFilteredState.value = result
            }
        }
    }

    fun setUnfilteredProducts(values: List<ProductDto>) {
        unfilteredProducts.clear()
        unfilteredProducts.addAll(values)
    }

    fun selectCategory(category: String) {
        _productsFilteredState.value = when (val current = _productsFilteredState.value) {
            is UIState.Success -> {
                if (category == "All") {
                    // Reset to original list
                    UIState.Success(unfilteredProducts)
                } else {
                    // Apply filter
                    UIState.Success(unfilteredProducts.filter { it.category == category })
                }
            }
            else -> current // Preserve other states
        }
    }


}