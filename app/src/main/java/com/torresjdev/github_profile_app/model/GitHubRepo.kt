package com.torresjdev.github_profile_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GitHubRepo(
    val name: String,
    val description: String?,
    val language: String?,
    @SerialName("stargazers_count") val stargazersCount: Int,
    @SerialName("html_url") val htmlUrl: String
)
