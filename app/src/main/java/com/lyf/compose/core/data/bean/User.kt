package com.lyf.compose.core.data.bean


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("admin")
    var admin: Boolean = false,
    @SerialName("coinCount")
    var coinCount: Int = 0,
    @SerialName("email")
    var email: String = "",
    @SerialName("icon")
    var icon: String = "",
    @SerialName("id")
    var id: Int = 0,
    @SerialName("nickname")
    var nickname: String = "",
    @SerialName("password")
    var password: String = "",
    @SerialName("publicName")
    var publicName: String = "",
    @SerialName("token")
    var token: String = "",
    @SerialName("type")
    var type: Int = 0,
    @SerialName("username")
    var username: String = ""
)