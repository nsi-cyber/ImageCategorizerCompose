package com.nsicyber.imagecategorizer.utils

import android.net.Uri
import android.os.Environment
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavBackStackEntry
import com.google.gson.Gson
import java.io.File
import java.net.URLEncoder
import java.util.Objects


fun getRealPathFromURI(uri: Uri?): String? {
    val paths: List<String>? = Objects.requireNonNull(uri?.path)?.split(":")

    return Environment.getExternalStorageDirectory().toString() +
            if (paths?.size!! > 1) File.separator + (paths.get(1)) else ""
}

fun getLastDirectoryName(uri: String?): String? {
    uri?.let {
        return try {

            // Uri'yi "/" karakterine göre böleriz
            val splittedUri = removePrimary3A(uri).split("/")

            // Bölünen dizinin son elemanını döndürürüz
            splittedUri.last()
        } catch (e:Exception){
            null
        }
    }
    return null

}

fun removePrimary3A(input: String): String {
    val targetString = "primary%3A"

    if (input.contains(targetString)) {
        val startIndex = input.indexOf(targetString)
        val endIndex = startIndex + targetString.length

        return input.removeRange(startIndex, endIndex)
    }

    return input
}


fun <T> T.toJson(): String? {
    return Gson().toJson(this)
}
fun <T> String.fromJson(type: Class<T>): T? {
    try {
        return Gson().fromJson(this, type);
    } catch (e: Exception) {
        print(e)
    }
    return null;
}



fun Modifier.noRippleClickable(onClick: () -> Unit): Modifier = composed {
    clickable (indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}

fun String?.encode():String?{
    return this?.replace(" ", "%20")?.replace("(", "%28")
        ?.replace(")", "%29")
}
fun String?.encodeUTF():String?{
    return String(this!!.toByteArray(), Charsets.UTF_8)
}



