package com.nsicyber.imagecategorizer.di


import android.app.Application
import com.nsicyber.imagecategorizer.interfaces.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContextProvider(application: Application): ContextProvider {
        return AppContextProvider(application)
    }



}
