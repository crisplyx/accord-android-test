package id.rasyiid.accord_android_test.domain.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeolocationDto(
    val lat: String,
    val long: String
)