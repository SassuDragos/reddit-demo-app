package com.sogard.data.apis

import com.sogard.data.models.AccessTokenDAO
import io.reactivex.Single
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthenticationApi {

    @POST("/api/v1/access_token")
    fun getAccessToken(
        @Query("grant_type") grantType: String,
        @Query("device_id") deviceId: String
    ): Single<AccessTokenDAO>
}