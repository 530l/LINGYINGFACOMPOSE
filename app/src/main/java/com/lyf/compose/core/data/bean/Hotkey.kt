package com.lyf.compose.core.data.bean


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Hotkey(
    @SerialName("id")
    var id: Int = 0,
    @SerialName("link")
    var link: String = "",
    @SerialName("name")
    var name: String = "",
    @SerialName("order")
    var order: Int = 0,
    @SerialName("visible")
    var visible: Int = 0
)