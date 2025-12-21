package com.lyf.compose.core.data.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Banner(
    @SerialName("desc")
    var desc: String = "",
    @SerialName("id")
    var id: Int = 0,
    @SerialName("imagePath")
    var imagePath: String = "",
    @SerialName("isVisible")
    var isVisible: Int = 0,
    @SerialName("order")
    var order: Int = 0,
    @SerialName("title")
    var title: String = "",
    @SerialName("type")
    var type: Int = 0,
    @SerialName("url")
    var url: String = ""
) : Parcelable