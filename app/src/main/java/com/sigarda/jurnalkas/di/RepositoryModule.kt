package com.sigarda.jurnalkas.di

import com.sigarda.jurnalkas.data.repository.AuthApiRepository
import com.sigarda.jurnalkas.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideAuthRepository(authRepositoryImpl: UserRepositoryImpl): AuthApiRepository
}