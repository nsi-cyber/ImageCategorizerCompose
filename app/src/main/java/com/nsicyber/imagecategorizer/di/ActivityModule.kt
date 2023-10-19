package com.nsicyber.imagecategorizer.di

import android.app.Application
import android.content.Context
import com.nsicyber.imagecategorizer.interfaces.ContextProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped


@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @ActivityScoped
    @Provides
    fun provideContextProvider(application: Application): ContextProvider {
        return AppContextProvider(application)
    }
}


class AppContextProvider(private val application: Application) : ContextProvider {
    override fun getContext(): Context {
        return application
    }
}
