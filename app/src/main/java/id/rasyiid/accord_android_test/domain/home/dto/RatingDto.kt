package id.rasyiid.accord_android_test.domain.home.dto

import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    val rate: Double,
    val count: Int
)
