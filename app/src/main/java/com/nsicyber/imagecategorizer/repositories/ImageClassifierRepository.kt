package com.nsicyber.imagecategorizer.repositories

import android.content.Context
import android.graphics.Bitmap
import com.nsicyber.imagecategorizer.interfaces.ImageAnalyzer
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ImageClassifierRepository @Inject constructor() : ImageAnalyzer {

    private var classifier: ImageClassifier? = null

    fun initClassifier(context: Context) {
        val baseOptions = BaseOptions.builder().setNumThreads(2).build()
        val options = ImageClassifier.ImageClassifierOptions.builder().setBaseOptions(baseOptions)
            .setMaxResults(2).setScoreThreshold(0.5f).build()
        try {
            classifier = ImageClassifier.createFromFileAndOptions(
                context,
                "model_object_detection_mobile_object_labeler_v1_1.tflite",
                options
            )
        } catch (e: Exception) {
        }

    }

    override fun classify(bitmap: Bitmap?): String? {

        val imageProcessor = ImageProcessor.Builder().build()
        val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

        val imageProcessingOptions = ImageProcessingOptions.builder()
            .build()

        val results = classifier?.classify(tensorImage, imageProcessingOptions)

        return results?.flatMap { classications ->
            classications.categories.map { category ->
                category.displayName
            }
        }?.firstOrNull().toString()


    }

}
