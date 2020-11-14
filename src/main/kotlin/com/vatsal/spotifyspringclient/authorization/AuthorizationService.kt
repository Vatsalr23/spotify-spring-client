package com.vatsal.spotifyspringclient.authorization

import com.vatsal.spotifyspringclient.common.Tokens
import com.vatsal.spotifyspringclient.common.exception.SpotifyAPIException
import com.vatsal.spotifyspringclient.integration.spotify.authorization.AuthorizationApiClient
import com.vatsal.spotifyspringclient.integration.spotify.authorization.TokenResponse
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthorizationService(
	private val authorizationApiClient: AuthorizationApiClient,
	@Value("\${spotify.client-id}") private val clientId: String,
	@Value("\${spotify.client-secret}") private val clientSecret: String,
	@Value("\${spotify.authorization.redirect-uri}") private val redirectURI: String,
	@Value("\${spotify.scope}") private val scope: String
) {

	companion object: KLogging()

	fun getSpotifyRedirectURL(): String {
		val state = UUID.randomUUID().toString()

		logger.debug { "state set = $state" }

		val response = authorizationApiClient.authorize(
			clientId = clientId,
			redirectURI = redirectURI,
			state = state,
			scope = scope
		).execute()

		if(!response.isSuccessful) {
			val code = response.code()
			val errorResponse = response.errorBody()?.string()
			throw SpotifyAPIException("Authorization request failed. Code=[$code] Error=[$errorResponse]")
		}

		logger.info { "Authorization request was successful. Redirecting..." }

		return response.raw().request().url().url().toString()
	}

	fun fetchTokens(
		code: String?,
		state: String?,
		error: String?
	): TokenResponse {
		state?.let {
			logger.debug { "state received: $state" }
		}

		error?.let {
			throw SpotifyAPIException("An error occurred while authorizing. Error reason = $error")
		}

		checkNotNull(code) {
			"No code provided in the callback method."
		}.let {
			val clientIdAndSecretInBase64 = "Basic ${getClientIdAndSecretInBase64()}"

			logger.debug { "Fetching tokens." }

			val response = authorizationApiClient.getTokens(
				authorization = clientIdAndSecretInBase64,
				code = it,
				redirectURI = redirectURI
			).execute()

			if(!response.isSuccessful) {
				val responseCode = response.code()
				val errorResponse = response.errorBody()?.string()
				throw SpotifyAPIException("Fetch token request failed. Code=[$responseCode] Error=[$errorResponse]")
			}

			val body = checkNotNull(response.body()) {
				"Response body should not be null."
			}

			logger.info { "Fetching tokens was successful." }

			setTokens(body)

			return body
		}
	}

	fun refreshAccessToken(): TokenResponse {
		val refreshToken = checkNotNull(Tokens.refreshToken) {
			"Refresh token is not set."
		}

		val clientIdAndSecretInBase64 = "Basic ${getClientIdAndSecretInBase64()}"
		val response = authorizationApiClient.getAccessToken(
			authorization = clientIdAndSecretInBase64,
			refreshToken = refreshToken
		).execute()

		if(!response.isSuccessful) {
			val responseCode = response.code()
			val errorResponse = response.errorBody()?.string()
			throw SpotifyAPIException("Fetch token request failed. Code=[$responseCode] Error=[$errorResponse]")
		}

		val body = checkNotNull(response.body()) {
			"Response body should not be null."
		}

		// Updates access token only since refresh token would be null
		setTokens(body)

		return body
	}

	private fun setTokens(body: TokenResponse) {
		logger.info { "Updating Tokens. Previous token values:\n" +
			"accessToken: ${Tokens.accessToken}\n" +
			"refreshToken: ${Tokens.refreshToken}\n" }

		Tokens.accessToken = body.accessToken
		Tokens.refreshToken = body.refreshToken ?: Tokens.refreshToken
		Tokens.isTokenSet = true

		logger.info { "New token values:\n" +
			"accessToken: ${Tokens.accessToken}\n" +
			"refreshToken: ${Tokens.refreshToken}\n" }
	}

	private fun getClientIdAndSecretInBase64(): String {
		val clientIdAndSecret = "$clientId:$clientSecret"
		return Base64.getEncoder().encodeToString(clientIdAndSecret.toByteArray())
	}

}
