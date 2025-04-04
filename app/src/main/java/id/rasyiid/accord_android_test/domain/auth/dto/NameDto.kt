package id.rasyiid.accord_android_test.domain.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class NameDto(
    val firstname: String,
    val lastname: String
)