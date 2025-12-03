package com.torresjdev.github_profile_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
