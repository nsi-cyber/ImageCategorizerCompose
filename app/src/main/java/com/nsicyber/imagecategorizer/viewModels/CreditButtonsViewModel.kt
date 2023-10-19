package com.nsicyber.imagecategorizer.viewModels


import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class CreditButtonsViewModel @Inject constructor() : ViewModel() {

    fun giveRating(context: Context) {
        val manager = ReviewManagerFactory.create(context)
        manager
            .requestReviewFlow()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    manager.launchReviewFlow(context as Activity, task.result)
                }
            }
    }


    fun shareApp(context: Context){
        ContextCompat.startActivity(
            context, Intent.createChooser(
                Intent(Intent.ACTION_SEND)
                    .setType("text/plain")
                    .putExtra(
                        Intent.EXTRA_TEXT,
                        "Download this fantastic app and share with your friends." +
                                " \n\n https://play.google.com/store/apps/details?id:" + context.packageName
                    ), "Choose one"
            ), null
        )
    }




}
