package com.sogard.data.models

import com.squareup.moshi.Json

data class AccessTokenDAO(
    @field:Json(name = "access_token") val token: String,
    @field:Json(name = "expires_in") val secondsToExpiration: Int,
    @field:Json(name = "token_type") val tokenType: String,
    @field:Json(name = "device_id") val deviceId: String
)
