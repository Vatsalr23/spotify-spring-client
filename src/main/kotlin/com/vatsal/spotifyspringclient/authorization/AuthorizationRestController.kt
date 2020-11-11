package com.vatsal.spotifyspringclient.authorization

import com.vatsal.spotifyspringclient.integration.spotify.SpotifyAuthApiClient
import com.vatsal.spotifyspringclient.integration.spotify.TokenResponse
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView
import java.util.*

@RestController
@RequestMapping("/v1")
class AuthorizationRestController(
    private val spotifyAuthApiClient: SpotifyAuthApiClient,
    @Value("\${spotify.client-id}") private val clientId: String,
    @Value("\${spotify.client-secret}") private val clientSecret: String,
    @Value("\${spotify.authorization.redirect-uri}") private val redirectURI: String,
    private val authorizationService: AuthorizationService
) {

    companion object: KLogging()

    @GetMapping("/callback")
    fun callback(
        @RequestParam(value = "code") code: String? = null,
        @RequestParam(value = "error") error: String? = null,
        @RequestParam(value = "state") state: String? = null
    ): TokenResponse {
        logger.info { "Callback received. Fetching tokens." }
        return authorizationService.fetchTokens(
            code = code,
            error = error,
            state = state
        )
    }

    @GetMapping("/login")
    fun login(): RedirectView {
        logger.info { "Received request to login." }
        return RedirectView(authorizationService.getSpotifyRedirectURL())
    }
}