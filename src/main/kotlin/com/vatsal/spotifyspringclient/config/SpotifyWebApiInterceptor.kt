package com.vatsal.spotifyspringclient.config

import com.vatsal.spotifyspringclient.authorization.AuthorizationService
import okhttp3.Interceptor
import okhttp3.Response

class SpotifyWebApiInterceptor(
	private val authorizationService: AuthorizationService
): Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		// TODO: Update implementation such that a new accessToken is not fetched every api call
		val tokens = authorizationService.refreshAccessToken()
		val requestWithToken = chain.request().newBuilder()
			.addHeader("authorization", "Bearer ${tokens.accessToken}")
			.build()
		return chain.proceed(requestWithToken)
	}
}