package id.rasyiid.accord_android_test.domain.auth.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.auth.AuthRepository
import id.rasyiid.accord_android_test.domain.auth.dto.SignInResponseDto
import id.rasyiid.accord_android_test.views.UIState
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

@ViewModelScoped
class SignInUseCase @Inject constructor (private val repository: AuthRepository) {
    suspend fun invoke(username: String, password: String): Flow<UIState<SignInResponseDto>> = repository.doSignIn(username, password)
}