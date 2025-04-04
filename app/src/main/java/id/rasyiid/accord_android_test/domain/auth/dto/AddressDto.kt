package id.rasyiid.accord_android_test.domain.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    val geolocation: GeolocationDto,
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String
)