package com.sogard.data.models

import com.squareup.moshi.Json

data class AccessTokenDAO(
    @Json(name = "access_token") val token: String,
    @Json(name = "expires_in") val secondsToExpiration: Int,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "device_id") val deviceId: String
)
