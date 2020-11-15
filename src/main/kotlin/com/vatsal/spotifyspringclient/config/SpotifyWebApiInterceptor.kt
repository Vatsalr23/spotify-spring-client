package com.vatsal.spotifyspringclient.config

import com.vatsal.spotifyspringclient.authorization.AuthorizationService
import com.vatsal.spotifyspringclient.common.Tokens
import mu.KLogging
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection

class SpotifyWebApiInterceptor(
	private val authorizationService: AuthorizationService
): Interceptor {
	companion object: KLogging()

	override fun intercept(chain: Interceptor.Chain): Response {
		logger.debug { "Inserting access token in the request header." }
		val requestWithToken = updatedRequestWithAccessToken(chain.request())
		val response = chain.proceed(requestWithToken)

		// If response code is 401 (Unauthorized) then refresh access token.
		if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			logger.info { "Token is expired, refreshing token." }
			authorizationService.refreshAccessToken()
			val requestWithRefreshedToken = updatedRequestWithAccessToken(chain.request())
			return chain.proceed(requestWithRefreshedToken)
		}

		return response
	}

	private fun updatedRequestWithAccessToken(request: Request): Request = request.newBuilder()
		.addHeader("authorization", "Bearer ${Tokens.accessToken}")
		.build()
}