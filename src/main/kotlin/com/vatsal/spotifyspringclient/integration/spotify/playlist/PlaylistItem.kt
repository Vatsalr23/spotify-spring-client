package com.vatsal.spotifyspringclient.integration.spotify.playlist

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlaylistItem(
	@JsonProperty("name")
	val name: String,
	@JsonProperty("id")
	val id: String,
	@JsonProperty("description")
	val description: String
)
