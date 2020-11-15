package com.vatsal.spotifyspringclient.authorization

import com.vatsal.spotifyspringclient.integration.spotify.authorization.TokenResponse
import mu.KLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

@RestController
@RequestMapping("/v1")
class AuthorizationRestController(
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

    @GetMapping("/refreshTokens")
    fun refreshAccessToken(): TokenResponse {
        logger.info { "Refresh token request received. Refreshing access token." }
        return authorizationService.refreshAccessToken()
    }
}
