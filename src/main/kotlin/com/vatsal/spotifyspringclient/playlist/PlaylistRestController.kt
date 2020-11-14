package com.vatsal.spotifyspringclient.playlist

import com.vatsal.spotifyspringclient.integration.spotify.playlist.PlaylistResponse
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/playlist")
class PlaylistRestController(
	private val playlistService: PlaylistService
) {

	companion object: KLogging()

	@GetMapping("/user/all")
	fun fetchUserPlaylists(
		@RequestParam("limit") limit: Int,
		@RequestParam("offset", defaultValue = "0") offset: Int = 0
	): PlaylistResponse? {
		logger.info { "Fetch playlists request received." }
		return playlistService.fetchUserPlaylists(limit, offset)
	}

}
