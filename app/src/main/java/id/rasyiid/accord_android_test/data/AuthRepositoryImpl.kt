package id.rasyiid.accord_android_test.data

import id.rasyiid.accord_android_test.BuildConfig.*
import id.rasyiid.accord_android_test.domain.auth.AuthRepository
import id.rasyiid.accord_android_test.domain.auth.dto.SignInRequestDto
import id.rasyiid.accord_android_test.domain.auth.dto.SignInResponseDto
import id.rasyiid.accord_android_test.domain.auth.dto.UserDto
import id.rasyiid.accord_android_test.views.UIState
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor (private val httpClient: HttpClient): AuthRepository {
    override suspend fun doSignIn(
        username: String,
        password: String
    ): Flow<UIState<SignInResponseDto>> {
        return flow<UIState<SignInResponseDto>> {
            emit(UIState.Loading)
            try {
                val url = "${BASE_URL}/auth/login"
                val response: HttpResponse = httpClient.request {
                    method = HttpMethod.Post
                    url(url)
                    setBody(SignInRequestDto(username, password))
                }
                if(response.status == HttpStatusCode.OK) {
                    emit(UIState.Success(response.body<SignInResponseDto>()))
                } else {
                    emit(UIState.Error("Error"))
                }
            } catch (error: Exception) {
                emit(UIState.Error(error.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getUserDetail(token: String?, userId: Int): Flow<UIState<UserDto>> {
        return flow<UIState<UserDto>> {
            emit(UIState.Loading)
            try {
                val url = "${BASE_URL}/users/${userId}"
                val response: HttpResponse = httpClient.request {
                    method = HttpMethod.Get
                    url(url)
                    token?.let {
                        headers {
                            append("Authorization", "Bearer $it")
                        }
                    }
                }
                if(response.status == HttpStatusCode.OK) {
                    emit(UIState.Success(response.body<UserDto>()))
                } else {
                    emit(UIState.Error("Error"))
                }
            } catch (error: Exception) {
                emit(UIState.Error(error.message.toString()))
            }
        }.flowOn(Dispatchers.IO)
    }
}