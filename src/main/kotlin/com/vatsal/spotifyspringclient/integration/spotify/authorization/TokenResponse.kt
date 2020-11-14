package com.vatsal.spotifyspringclient.integration.spotify.authorization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TokenResponse(
	@JsonProperty("access_token")
	val accessToken: String,
	@JsonProperty("token_type")
	val tokenType: String,
	@JsonProperty("scope")
	val scope: String,
	@JsonProperty("expires_in")
	val expiresIn: Long,
	@JsonProperty("refresh_token")
	val refreshToken: String?
)
