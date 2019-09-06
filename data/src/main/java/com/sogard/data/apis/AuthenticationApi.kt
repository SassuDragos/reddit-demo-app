package com.sogard.data.apis

/**
 * API in charge with handling Reddit authentication.
 */
import com.sogard.data.models.AccessTokenDAO
import io.reactivex.Single
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Request fetching and returning the application access token for the API.
 *
 *  [grant_type]: Sets the authorization flow. Use the Application Only OAuth grant type
 *  [DEFAULT_GRANT_TYPE] (https://github.com/reddit-archive/reddit/wiki/oauth2)
 *
 *  [device_id]: Unique identifier (20-30 random string) for the host device.
 *
 *
 *  NOTE: This API is through Basic Auth and uses a different BASE_URL ([BASE_URL_OPEN]).
 */
interface AuthenticationApi {

    @POST("/api/v1/access_token")
    fun getAccessToken(
        @Query("grant_type") grantType: String,
        @Query("device_id") deviceId: String
    ): Single<AccessTokenDAO>
}