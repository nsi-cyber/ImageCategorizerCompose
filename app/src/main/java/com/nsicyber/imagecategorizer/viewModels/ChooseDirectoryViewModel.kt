package com.nsicyber.imagecategorizer.viewModels


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nsicyber.imagecategorizer.models.ClassifiedFolderModel
import com.nsicyber.imagecategorizer.repositories.ImageClassifierRepository
import com.nsicyber.imagecategorizer.utils.encodeUTF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class ChooseDirectoryViewModel @Inject constructor(private val imageClassifierRepository: ImageClassifierRepository) :
    ViewModel() {
    var mCheckedState =
        mutableStateOf(false)


    var selectedFolderUri =
        mutableStateOf<String?>(null)

    var showWarning =
        mutableStateOf(false)

    var warningText =
        mutableStateOf<String?>(null)

    var loader = mutableStateOf<Boolean?>(false)


    val handler = android.os.Handler()

    var classifiedFolderResult = mutableStateOf<List<ClassifiedFolderModel>>(listOf())

    fun openFolderPicker(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        return intent
    }

    fun showWarningWithDelay(text: String?) {
        warningText.value = text
        showWarning.value = true
        handler.postDelayed({
            showWarning.value = false
        }, 3000) // 10 saniye (10000 milisaniye) süreyle beklet
    }

    fun getImageListFromFolder(): List<String?>? {
        val photoExtensions = listOf(".jpg", ".jpeg", ".png") // Fotoğraf dosya uzantıları
        val photoFilePaths = mutableListOf<String>() // Fotoğraf dosya yollarını içerecek liste

        val directory = File(selectedFolderUri.value + "/")
        if (directory.isDirectory) {
            // Ana klasördeki fotoğraf dosyalarını ekle
            val files = directory.listFiles()
            println(files)
            files?.forEach { file ->
                if (file.isFile && photoExtensions.any { file.name.endsWith(it) }) {
                    photoFilePaths.add(
                        file.absolutePath.encodeUTF().toString()
                    )
                }
            }

            if (mCheckedState.value == true) {
                // Alt klasörlerdeki fotoğraf dosyalarını ekle
                val subdirectories = files?.filter { it.isDirectory }
                subdirectories?.forEach { subdirectory ->
                    val subFiles = subdirectory.listFiles()
                    subFiles?.forEach { file ->
                        if (file.isFile && photoExtensions.any { file.name.endsWith(it) }) {
                            photoFilePaths.add(
                                    file.absolutePath.encodeUTF().toString()
                            )                        }
                    }
                }
            }
        }

        return photoFilePaths.toList()


    }

    fun resizeAndConvertToBitmap(context: Context, sourceUri: String?): Bitmap? {
        try {
            // Fotoğrafı yükle
            val inputStream = context.contentResolver.openInputStream(Uri.parse(sourceUri))
            val sourceBitmap = BitmapFactory.decodeStream(inputStream)

            // Hedef boyutları belirle (224x224)
            val targetWidth = 224
            val targetHeight = 224

            // Fotoğrafı hedef boyuta yeniden boyutlandır
            val resizedBitmap: Bitmap
            if (sourceBitmap.width <= targetWidth && sourceBitmap.height <= targetHeight) {
                // Eğer fotoğraf hedef boyuttan küçükse, boyutlandırmaya gerek yok
                resizedBitmap = sourceBitmap
            } else {
                val scaleWidth = targetWidth.toFloat() / sourceBitmap.width
                val scaleHeight = targetHeight.toFloat() / sourceBitmap.height
                val scaleFactor = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight

                val matrix = Matrix()
                matrix.postScale(scaleFactor, scaleFactor)

                resizedBitmap = Bitmap.createBitmap(
                    sourceBitmap, 0, 0, sourceBitmap.width, sourceBitmap.height, matrix, true
                )
            }

            // Eğer hedef boyuta ulaşıldıysa, bitmap'i kaydet
            val outputFile = File(context.cacheDir, "resized_image.bmp")
            val outputStream = FileOutputStream(outputFile)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()

            return resizedBitmap
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
    }


    fun classifyImages(context: Context, onFinish: () -> Unit? = {}) {
        viewModelScope.launch(Dispatchers.IO) { // IO thread'i kullanarak işlemi ayırın
            var counter = 1
            imageClassifierRepository.initClassifier(context)
            val temp = getImageListFromFolder()

            // HashMap oluştur
            val resultMap = HashMap<String, MutableList<String>>()

            temp?.forEach { uri ->
                val result = imageClassifierRepository.classify(resizeAndConvertToBitmap(context, "file:///"+uri))
                if (result != null&&result!="null") {
                    val resultList = resultMap.getOrPut(result) { mutableListOf() }
                    resultList.add(uri.toString())
                }
                counter++
            }

            // Ana thread'e geri dönerek UI güncellemelerini yapın
            withContext(Dispatchers.Main) {
                for ((title, images) in resultMap) {
                    classifiedFolderResult.value += ClassifiedFolderModel(title, images)
                }
                showWarning.value = false
                loader.value = false
                onFinish()
            }
        }
    }
}