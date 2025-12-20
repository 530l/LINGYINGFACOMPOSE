package com.lyf.compose.core.data.api

import com.lyf.compose.core.data.api.request.GoodsSearchRequest
import com.lyf.compose.core.data.bean.Auth
import com.lyf.compose.core.data.bean.Goods
import com.lyf.compose.core.data.bean.User
import com.lyf.compose.core.data.network.NetworkPageData
import com.lyf.compose.core.data.network.NetworkResponse

interface AtmobApi {


    /**
     * 密码登录
     * @param params 密码登录请求参数
     * @return 认证信息响应
     */
    suspend fun loginByPassword(params: Map<String, String>): NetworkResponse<Auth>

    /**
     * 分页查询商品
     * @return 商品分页数据响应
     */
    suspend fun getGoodsPage(params: GoodsSearchRequest): NetworkResponse<NetworkPageData<Goods>>

    /**
     * 获取商品信息
     * @param id 商品ID
     * @return 商品信息响应
     */
    suspend fun getGoodsInfo(id: String): NetworkResponse<Goods>

    /**
     * 获取用户个人信息
     * @return 用户信息响应
     */
    suspend fun getPersonInfo(): NetworkResponse<User>


}