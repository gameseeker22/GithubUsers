package com.example.githubusers.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 1 data class is used since the data fetch
 * from api is as good as one to one.
 */

@Entity(tableName = "githubUsers")
data class User(

    @SerializedName("login")
    val login: String = "",
    @PrimaryKey
    @SerializedName("id")
    val id: Long = 0,
    @SerializedName("node_id")
    val node_id: String = "",
    @SerializedName("avatar_url")
    val avatar_url: String = "",
    @SerializedName("gravatar_id")
    val gravatar_id: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("html_url")
    val html_url: String = "",
    @SerializedName("followers_url")
    val followers_url: String = "",
    @SerializedName("following_url")
    val following_url: String = "",
    @SerializedName("gists_url")
    val gists_url: String = "",
    @SerializedName("starred_url")
    val starred_url: String = "",
    @SerializedName("subscriptions_url")
    val subscriptions_url: String = "",
    @SerializedName("organizations_url")
    val organizations_url: String = "",
    @SerializedName("repos_url")
    val repos_url: String = "",
    @SerializedName("events_url")
    val events_url: String = "",
    @SerializedName("received_events_url")
    val received_events_url: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("site_admin")
    val site_admin: Boolean = false,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("company")
    val company: String? = null,
    @SerializedName("blog")
    val blog: String? = null,
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("hireable")
    val hireable: String? = null,
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("twitter_username")
    val twitter_username: String? = null,
    @SerializedName("public_repos")
    val public_repos: Int = 0,
    @SerializedName("public_gists")
    val public_gists: Int = 0,
    @SerializedName("followers")
    val followers: Int = 0,
    @SerializedName("following")
    val following: Int = 0,
    @SerializedName("created_at")
    val created_at: String = "",
    @SerializedName("updated_at")
    val updated_at: String = "",
    @SerializedName("note")
    val note: String = ""
) : Serializable