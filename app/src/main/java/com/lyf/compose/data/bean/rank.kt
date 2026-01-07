package com.lyf.compose.data.bean

data class RankingResponse(
    val code: Int,
    val traceId: String,
    val data: RankingData
)

data class RankingData(
    val list: List<RankingItem>
)

data class RankingItem(
    val id: String,
    val title: String,
    val type: String,
    val reward: String,
    val works: List<Work>
)

data class Work(
    val workId: String,
    val ssid: String,
    val taskId: String,
    val sourceId: String,
    val title: String,
    val workType: String,
    val subType: String,
    val imageUrl: String,
    val audioUrl: String,
    val amountPraise: Int,
    val userName: String,
    val headImgUrl: String
)