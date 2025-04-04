package id.rasyiid.accord_android_test.domain.auth.usecases

import dagger.hilt.android.scopes.ViewModelScoped
import id.rasyiid.accord_android_test.domain.auth.AuthRepository
import id.rasyiid.accord_android_test.domain.auth.dto.UserDto
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetUserDetailUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend fun invoke(token: String?, userId: Int): Flow<UIState<UserDto>> = repository.getUserDetail(token, userId)
}