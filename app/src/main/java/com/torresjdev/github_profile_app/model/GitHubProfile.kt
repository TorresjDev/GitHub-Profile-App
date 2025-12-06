package com.torresjdev.github_profile_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable data class that represents a GitHub profile.
 *
 * @property login The GitHub username.
 * @property avatarUrl The URL of the user's avatar.
 * @property name The user's full name.
 * @property bio A short biography or description.
 * @property publicRepos The number of public repositories the user has.
 * @property followers The number of followers the user has.
 * @property following The number of users the user is following.
 * @constructor Creates a new instance of [GitHubProfile].
 */
@Serializable
data class GitHubProfile(
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val name: String?,
    val bio: String?,
    @SerialName("public_repos") val publicRepos: Int,
    val followers: Int,
    val following: Int
)
