package com.ds.basetemplate.di

import com.ds.basetemplate.remote.ApiInterface
import com.ds.basetemplate.ui.home.data.repository_impl.HomeRepositoryImpl
import com.ds.basetemplate.ui.home.data.services.HomeComponentServices
import com.ds.basetemplate.ui.home.domain.repository.HomeRepository
import com.ds.basetemplate.ui.home.domain.usecase.HomeUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HomeRepositoryModule {

    @Provides
    @Singleton
    fun provideHomeComponentServices(apiInterface: ApiInterface):HomeComponentServices{
        return HomeComponentServices(apiInterface)
    }

    @Provides
    @Singleton
    fun provideHomeRepository(service:HomeComponentServices):HomeRepository{
        return HomeRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideHomeUseCase(repository: HomeRepository):HomeUsecase{
        return HomeUsecase(repository)
    }

}