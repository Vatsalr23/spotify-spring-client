package com.vatsal.spotifyspringclient.config

import com.vatsal.spotifyspringclient.authorization.AuthorizationService
import com.vatsal.spotifyspringclient.integration.spotify.authorization.AuthorizationApiClient
import com.vatsal.spotifyspringclient.integration.spotify.playlist.PlaylistApiClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class SpotifyClientConfig(
	@Value("\${spotify.auth.base-url}") val spotifyAuthBaseUrl: String,
	@Value("\${spotify.api.base-url}") val spotifyApiBaseUrl: String,
	// FYI: @Lazy breaks the circular dependency when creating the SpotifyWebApiInterceptor
	@Lazy private val authorizationService: AuthorizationService
) {

	@Bean
	@Qualifier("SpotifyAuthRetrofit")
	fun createAuthRetrofit(): Retrofit {
		val okHttpClient = OkHttpClient().newBuilder()
			.followRedirects(true)
			.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
			.build()

		return Retrofit.Builder()
			.addConverterFactory(JacksonConverterFactory.create())
			.client(okHttpClient)
			.baseUrl(spotifyAuthBaseUrl)
			.build()
	}

	@Bean
	fun spotifyAuthClient(
		@Qualifier("SpotifyAuthRetrofit")
		retrofit: Retrofit
	): AuthorizationApiClient {
		return retrofit.create(AuthorizationApiClient::class.java)
	}

	@Bean
	@Qualifier("SpotifyApiRetrofit")
	fun createApiRetrofit(): Retrofit {
		val okHttpClient = OkHttpClient().newBuilder()
			.addInterceptor(SpotifyWebApiInterceptor(authorizationService))
			.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
			.build()

		return Retrofit.Builder()
			.addConverterFactory(JacksonConverterFactory.create())
			.client(okHttpClient)
			.baseUrl(spotifyApiBaseUrl)
			.build()
	}

	@Bean
	fun spotifyWebApiClient(
		@Qualifier("SpotifyApiRetrofit")
		retrofit: Retrofit
	): PlaylistApiClient {
		return retrofit.create(PlaylistApiClient::class.java)
	}

}
