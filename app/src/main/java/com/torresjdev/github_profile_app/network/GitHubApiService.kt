package com.torresjdev.github_profile_app.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.torresjdev.github_profile_app.model.GitHubProfile
import com.torresjdev.github_profile_app.model.GitHubRepo
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

/** Base URL for all GitHub API requests. */
private const val BASE_URL = "https://api.github.com/"

/**
 * Retrofit instance configured for GitHub API.
 * Uses kotlinx.serialization for JSON parsing with unknown keys ignored.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

/**
 * Retrofit service interface for GitHub API endpoints.
 * Defines suspend functions for async network calls.
 */
interface GitHubApiService {

    /**
     * Fetches a GitHub user's profile information.
     * @param username The GitHub username to look up.
     * @return [GitHubProfile] containing the user's profile data.
     */
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): GitHubProfile

    /**
     * Fetches all public repositories for a GitHub user.
     * @param username The GitHub username whose repos to retrieve.
     * @return List of [GitHubRepo] objects for the user.
     */
    @GET("users/{username}/repos")
    suspend fun getRepos(@Path("username") username: String): List<GitHubRepo>
}

/**
 * Singleton object providing access to the GitHub API service.
 * Use [retrofitService] to make API calls.
 */
object GitHubApi {

    /** Lazily initialized Retrofit service instance for GitHub API calls. */
    val retrofitService: GitHubApiService by lazy {
        retrofit.create(GitHubApiService::class.java)
    }
}
