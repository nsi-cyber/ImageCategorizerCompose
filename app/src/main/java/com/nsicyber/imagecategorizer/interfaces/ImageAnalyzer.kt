package com.nsicyber.imagecategorizer.interfaces

import android.graphics.Bitmap


interface ImageAnalyzer {
    fun classify(bitmap: Bitmap?):String?
}