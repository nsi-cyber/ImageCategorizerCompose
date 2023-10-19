package com.nsicyber.imagecategorizer

import android.app.Application
import android.content.Context
import com.nsicyber.imagecategorizer.interfaces.ContextProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImageCategorizerApplication : Application(), ContextProvider {
    override fun getContext(): Context {
        return this
    }
}