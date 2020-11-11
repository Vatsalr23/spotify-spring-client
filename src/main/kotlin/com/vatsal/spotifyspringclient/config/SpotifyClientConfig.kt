package com.vatsal.spotifyspringclient.config

import com.vatsal.spotifyspringclient.integration.spotify.SpotifyAuthApiClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Configuration
class SpotifyClientConfig(
	@Value("\${spotify.auth.base-url}") val spotifyAuthBaseUrl: String
) {

	@Bean
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
	fun spotifyAuthClient(retrofit: Retrofit): SpotifyAuthApiClient {
		return retrofit.create(SpotifyAuthApiClient::class.java)
	}

}
