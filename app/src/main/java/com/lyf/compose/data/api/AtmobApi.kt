package com.lyf.compose.data.api


import com.lyf.compose.data.bean.Article
import com.lyf.compose.data.bean.Banner
import com.lyf.compose.data.bean.Hotkey
import com.lyf.compose.data.bean.User
import com.lyf.compose.data.network.NetworkResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.GET
import retrofit2.http.Path

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


    @POST("/hotkey/json")
    suspend fun getHotKey(): NetworkResponse<List<Hotkey>>

    // WanAndroid 分页文章
    @GET("/article/list/{page}/json")
    suspend fun getArticleList(
        @Path("page") page: Int
    ): NetworkResponse<Article>


}