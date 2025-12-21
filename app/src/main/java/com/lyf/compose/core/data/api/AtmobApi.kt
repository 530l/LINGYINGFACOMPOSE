package com.lyf.compose.core.data.api

import com.lyf.compose.core.data.bean.Banner
import com.lyf.compose.core.data.bean.User
import com.lyf.compose.core.data.network.NetworkResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AtmobApi {

    // ❌ 错误：用了 @Body + JSON
//    @POST("/user/login")
//    suspend fun loginByPassword(@Body params: LoginRequest): NetworkResponse<User>

    //  ✅ 正确：用了 @Field + Form
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun loginByPassword(
        @Field("username") username: String,
        @Field("password") password: String
    ): NetworkResponse<User>

    @POST("/banner/json")
    suspend fun getBanner(): NetworkResponse<List<Banner>>


}