package id.rasyiid.accord_android_test.domain.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDto(
    val username: String,
    val password: String
)
