package com.vatsal.spotifyspringclient.integration.spotify

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SpotifyAuthApiClient {

	@GET("authorize")
	fun authorize(
		@Query("client_id") clientId: String,
		@Query("response_type") responseType: String = "code",
		@Query("redirect_uri") redirectURI: String,
		@Query("scope") scope: String,
		@Query("state") state: String
	): Call<ResponseBody>

	@FormUrlEncoded
	@POST("api/token")
	fun getToken(
		@Header("Authorization") authorization: String,
		@Field("grant_type") grantType: String = "authorization_code",
		@Field("code") code: String,
		@Field("redirect_uri") redirectURI: String
	): Call<TokenResponse>
}
