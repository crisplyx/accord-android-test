package id.rasyiid.accord_android_test.views.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.rasyiid.accord_android_test.domain.auth.usecases.SignInUseCase
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor (
    private val signInUseCase: SignInUseCase,
    application: Application
) : AndroidViewModel(application) {
    private val _signInState = MutableStateFlow<UIState<Any>>(UIState.Idle)
    val signInState: StateFlow<UIState<Any>> = _signInState

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            signInUseCase.execute(username, password).collect { result ->
                _signInState.value = result
            }
        }
    }
}