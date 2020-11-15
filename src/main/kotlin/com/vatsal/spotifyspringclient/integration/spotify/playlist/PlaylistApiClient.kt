package com.vatsal.spotifyspringclient.integration.spotify.playlist

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaylistApiClient {

	@GET("v1/me/playlists")
	fun getCurrentUserPlaylists(
		@Query("limit") limit: Int,
		@Query("offset") offset: Int = 0
	): Call<PlaylistResponse>

}