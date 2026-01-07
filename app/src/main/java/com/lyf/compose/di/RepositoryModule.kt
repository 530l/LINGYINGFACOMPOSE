package com.lyf.compose.di


import com.lyf.compose.data.api.AtmobApi
import com.lyf.compose.data.network.ErrorHandler
import com.lyf.compose.data.repository.feature.AuthRepository
import com.lyf.compose.data.repository.feature.ContentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 仓库模块依赖注入
 *
 * 提供各业务领域仓库的实例。
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /**
     * 提供认证仓库
     *
     * 处理用户登录、注册等认证相关业务。
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        atmobApi: AtmobApi,
        errorHandler: ErrorHandler
    ): AuthRepository = AuthRepository(atmobApi)

    /**
     * 提供内容仓库
     *
     * 处理 Banner、文章、热词等内容相关业务。
     */
    @Provides
    @Singleton
    fun provideContentRepository(
        atmobApi: AtmobApi,
        errorHandler: ErrorHandler
    ): ContentRepository = ContentRepository(atmobApi)
}
