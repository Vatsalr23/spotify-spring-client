package com.vatsal.spotifyspringclient.playlist

import com.vatsal.spotifyspringclient.integration.spotify.playlist.PlaylistApiClient
import com.vatsal.spotifyspringclient.integration.spotify.playlist.PlaylistResponse
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class PlaylistService(
	private val playlistApiClient: PlaylistApiClient
){

	companion object: KLogging()

	fun fetchUserPlaylists(
		limit: Int,
		offset: Int
	): PlaylistResponse? {
		val response = playlistApiClient.getCurrentUserPlaylists(
			limit = limit,
			offset = offset
		).execute()

		return response.body()
	}

}