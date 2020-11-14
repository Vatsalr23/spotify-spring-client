package com.vatsal.spotifyspringclient.integration.spotify.playlist

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.vatsal.spotifyspringclient.integration.spotify.playlist.PlaylistItem

@JsonIgnoreProperties(ignoreUnknown = true)
data class PlaylistResponse (
	@JsonProperty("total")
	val total: Int,
	@JsonProperty("offset")
	val offset: Int,
	@JsonProperty("items")
	val items: List<PlaylistItem>
)