package com.lyf.compose.core.data.api.request

import kotlinx.serialization.Serializable

/**
 * 商品搜索分页请求模型
 *
 * @param page 页码
 * @param size 每页大小
 * @author Joker.X
 */
@Serializable
data class GoodsSearchRequest(
    /**
     * 页码
     */
    val page: Int = 1,

    /**
     * 每页大小
     */
    val size: Int = 20,
)