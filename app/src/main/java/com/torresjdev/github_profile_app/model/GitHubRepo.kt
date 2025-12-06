package com.torresjdev.github_profile_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data class representing a GitHub repository.
 *
 * @property name The name of the repository.
 * @property description The description of the repository.
 * @property language The programming language used in the repository.
 * @property stargazersCount The number of stars the repository has received.
 * @property htmlUrl The URL of the repository's HTML page.
 * @property updatedAt ISO 8601 timestamp of when the repository was last updated.
 * @constructor Creates a new instance of [GitHubRepo].
 */
@Serializable
data class GitHubRepo(
    val name: String,
    val description: String?,
    val language: String?,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("html_url") val htmlUrl: String,
    @SerialName("updated_at") val updatedAt: String
)
