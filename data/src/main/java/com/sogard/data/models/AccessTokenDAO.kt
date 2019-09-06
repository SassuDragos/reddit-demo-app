package com.sogard.data.models

import com.squareup.moshi.Json

/**
 * The DAO object used for authentication purposes.
 *
 * [token]: OAuth token which allows client access to protected API endpoints.
 * [secondsToExpiration]: The amount of seconds for which the token is valid.
 * [tokenType]: Authorization type
 * [deviceId]: A unique identifier used by the API to identify the device.
 */
data class AccessTokenDAO(
    @Json(name = "access_token") val token: String,
    @Json(name = "expires_in") val secondsToExpiration: Int,
    @Json(name = "token_type") val tokenType: String,
    @Json(name = "device_id") val deviceId: String
)
