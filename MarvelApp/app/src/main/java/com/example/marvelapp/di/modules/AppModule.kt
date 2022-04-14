package com.example.marvelapp.di.modules

import android.app.Application
import android.content.Context
import com.example.marvelapp.data.utils.MD5
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    fun provideMD5() = MD5()
}
