package com.sigarda.jurnalkas.di

import com.sigarda.jurnalkas.data.local.datasource.UserLocalDataSource
import com.sigarda.jurnalkas.data.local.datasource.UserLocalDataSourceImpl
import com.sigarda.jurnalkas.data.network.datasource.AuthRemoteDataSource
import com.sigarda.jurnalkas.data.network.datasource.AuthRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun provideAuthRemoteDataSource(authRemoteDataSourceImpl: AuthRemoteDataSourceImpl): AuthRemoteDataSource

    @Binds
    abstract fun provideUserLocalDataSource(userLocalDataSourceImpl: UserLocalDataSourceImpl): UserLocalDataSource
}