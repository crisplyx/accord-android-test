package id.rasyiid.accord_android_test.domain.auth.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val name: NameDto,
    val address: AddressDto,
    val phone: String,
    val __v: Int
)
