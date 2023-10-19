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
import com.nsicyber.imagecategorizer.models.ClassifiedFolderList
import com.nsicyber.imagecategorizer.models.ClassifiedFolderModel
import com.nsicyber.imagecategorizer.repositories.ImageClassifierRepository
import com.nsicyber.imagecategorizer.utils.encode
import com.nsicyber.imagecategorizer.utils.encodeUTF
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject


@HiltViewModel
class ClassifiedFolderViewModel @Inject constructor() :
    ViewModel() {


    val showDialog = mutableStateOf(false)
    val completed = mutableStateOf(false)
    var classifiedFolderListObj = mutableStateOf<ClassifiedFolderList?>(null)
    fun setupClassifiedResult(result: ClassifiedFolderList?) {
        classifiedFolderListObj.value = result
    }


    fun openFolderPicker(): Intent {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        return intent
    }

    fun copyImagesToFolder(model: List<ClassifiedFolderModel?>?, uri: String?) {
        showDialog.value=true
        viewModelScope.launch {
            model?.forEach { data ->

                val destinationPath = Paths.get(("$uri/${data?.title}").encodeUTF())
                if (!Files.exists(destinationPath)) {
                    try {
                        Files.createDirectories(destinationPath)
                    } catch (e: Exception) {
                        return@launch
                    }
                }

                if (data?.images != null) {

                    for (imagePath in data.images!!) {
                        val sourceFile = File(imagePath.encodeUTF())
                        if (sourceFile.exists() && sourceFile.isFile) {
                            val destinationFile = File(destinationPath.toFile(), sourceFile.name.encodeUTF())
                            try {
                                sourceFile.copyTo(destinationFile)
                            } catch (e: Exception) {
                                if(e is FileAlreadyExistsException)
                                println(e.toString())
                                else
                                return@launch
                            }
                        }
                    }


                }


            }
            showDialog.value=false
            completed.value=true


        }
    }


}