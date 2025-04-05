package id.rasyiid.accord_android_test.views.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rasyiid.accord_android_test.domain.auth.dto.SignInResponseDto
import id.rasyiid.accord_android_test.domain.auth.dto.UserDto
import id.rasyiid.accord_android_test.domain.auth.usecases.GetUserDetailUseCase
import id.rasyiid.accord_android_test.domain.auth.usecases.SignInUseCase
import id.rasyiid.accord_android_test.domain.product.usecases.ClearCartUseCase
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor (
    private val signInUseCase: SignInUseCase,
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val clearCartUseCase: ClearCartUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val _signInState = MutableStateFlow<UIState<SignInResponseDto>>(UIState.Idle)
    val signInState: StateFlow<UIState<SignInResponseDto>> = _signInState

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            signInUseCase.invoke(username, password).collect { result ->
                _signInState.value = result
            }
        }
    }

    private val _userDetailState = MutableStateFlow<UIState<UserDto>>(UIState.Idle)
    val userDetailState: StateFlow<UIState<UserDto>> = _userDetailState

    fun getUserDetail(token: String?, userId: Int) {
        viewModelScope.launch {
            getUserDetailUseCase.invoke(token, userId).collect { result ->
                _userDetailState.value = result
            }
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            clearCartUseCase.invoke()
        }
    }
}