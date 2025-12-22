package com.lyf.compose.core.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.lyf.compose.BuildConfig
import com.lyf.compose.core.data.api.AtmobApi
import com.lyf.compose.core.data.repositories.AtmobRepositories
import com.lyf.compose.core.interceptor.AuthInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * 网络模块
 * 负责提供网络相关的依赖注入
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = BuildConfig.BASE_URL

    /**
     * 提供JSON序列化配置
     * @return JSON序列化实例
     */
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    /**
     * 提供OkHttpClient实例
     * @param loggingInterceptor 日志拦截器
     * @param context 应用上下文
     * @return OkHttpClient实例
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) // 连接超时时间
            .writeTimeout(10, TimeUnit.SECONDS) // 写超时时间
            .readTimeout(10, TimeUnit.SECONDS) // 读超时时间
            // 鉴权：自动加 token + 401 清 token
            .addInterceptor(AuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(ChuckerInterceptor.Builder(context).build())
                }
            }
            // 请求失败重试
            .retryOnConnectionFailure(true)
            .build()
    }

    /**
     * 提供Retrofit实例
     * @param okHttpClient OkHttp客户端
     * @param json JSON序列化实例
     * @return Retrofit实例
     */
    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    /**
     * 提供日志拦截器
     * @return 日志拦截器实例
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    /**
     * 提供AtmobApi实例
     * @param retrofit Retrofit实例
     * @return AtmobApi实例
     */
    @Provides
    @Singleton
    fun provideAtmobApi(retrofit: Retrofit): AtmobApi {
        return retrofit.create(AtmobApi::class.java)
    }


    /**
     * 提供认证网络数据源实例
     * @param atmobApi AtmobApi实例
     * @return 认证网络数据源实例
     */
    @Provides
    @Singleton
    fun provideAtmobRepositories(atmobApi: AtmobApi): AtmobRepositories {
        return AtmobRepositories(atmobApi)
    }

}
