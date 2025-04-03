package id.rasyiid.accord_android_test.domain.auth

import id.rasyiid.accord_android_test.views.UIState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun doSignIn(username: String, password: String): Flow<UIState<Any>>
}