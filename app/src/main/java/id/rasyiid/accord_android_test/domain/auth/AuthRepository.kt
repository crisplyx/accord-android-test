package id.rasyiid.accord_android_test.domain.auth

import id.rasyiid.accord_android_test.domain.auth.dto.SignInResponseDto
import id.rasyiid.accord_android_test.domain.auth.dto.UserDto
import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun doSignIn(username: String, password: String): Flow<UIState<SignInResponseDto>>
    suspend fun getUserDetail(token: String?, userId: Int): Flow<UIState<UserDto>>
}