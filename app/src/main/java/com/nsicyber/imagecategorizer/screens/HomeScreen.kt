package com.nsicyber.imagecategorizer.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsicyber.imagecategorizer.BuildConfig
import com.nsicyber.imagecategorizer.R
import com.nsicyber.imagecategorizer.models.ClassifiedFolderList
import com.nsicyber.imagecategorizer.utils.getLastDirectoryName
import com.nsicyber.imagecategorizer.utils.getRealPathFromURI
import com.nsicyber.imagecategorizer.utils.noRippleClickable
import com.nsicyber.imagecategorizer.utils.toJson
import com.nsicyber.imagecategorizer.viewModels.ChooseDirectoryViewModel
import com.nsicyber.imagecategorizer.viewModels.CreditButtonsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navHostController: NavHostController) {
    val viewModel = hiltViewModel<ChooseDirectoryViewModel>()
    val context = LocalContext.current
    val uri = Uri.parse("package:${BuildConfig.APPLICATION_ID}")

    val storageWriteLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.loader.value = true
                    viewModel.warningText.value = " Images are classifying"
                    viewModel.showWarning.value = true

                    viewModel.classifyImages(context) {
                        navHostController.navigate(
                            "folder_screen?result=${ClassifiedFolderList(viewModel.classifiedFolderResult.value).toJson()}"
                        )
                    }
                }
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                ) {
                    try {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
                        intent.addCategory("android.intent.category.DEFAULT")
                        context.startActivity(intent)
                    } catch (ex: Exception) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        context.startActivity(intent)
                    }
                }
            }
        }

    val storageReadLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                CoroutineScope(Dispatchers.IO).launch { storageWriteLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE) }
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    try {
                        val intent =
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri)
                        intent.addCategory("android.intent.category.DEFAULT")
                        context.startActivity(intent)
                    } catch (ex: Exception) {
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        context.startActivity(intent)
                    }
                }
            }
        }


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                viewModel.selectedFolderUri.value = getRealPathFromURI(uri)
            }
        }

    val enterTransition = remember {
        expandVertically(expandFrom = Alignment.Top, animationSpec = tween(300)) +
                fadeIn(initialAlpha = 0.3f, animationSpec = tween(300))
    }
    val exitTransition = remember {
        shrinkVertically(shrinkTowards = Alignment.Top, animationSpec = tween(300)) +
                fadeOut(animationSpec = tween(300))
    }

    Column(Modifier.padding(horizontal = 16.dp, vertical = 32.dp)) {

        Text(
            text = "Classify Images",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF3C3E45), shape = RoundedCornerShape(size = 12.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Choose Directory:",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                )
            )

            Row(
                modifier = Modifier
                    .noRippleClickable { if (viewModel.loader.value == false) launcher.launch(viewModel.openFolderPicker()) }
                    .border(
                        width = 1.dp,
                        color = Color(0xFF505050),
                        shape = RoundedCornerShape(size = 16.dp)
                    )
                    .fillMaxWidth()
                    .background(color = Color(0x80000000), shape = RoundedCornerShape(size = 16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .padding(1.dp)
                        .size(48.dp),
                    painter = painterResource(id = R.drawable.solar_upload_bold_duotone),
                    contentDescription = "image description",
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = getLastDirectoryName(viewModel.selectedFolderUri.value)
                        ?: "Choose Folder to Categorize",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF756DF3),
                    )
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Switch(
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Color(0xFF756DF3),
                        uncheckedTrackColor = Color(0xCC000000),
                        checkedThumbColor = Color(0xFF756DF3),
                        uncheckedThumbColor = Color(0xFF756DF3)
                    ),
                    checked = viewModel.mCheckedState.value,
                    onCheckedChange = {
                        if (viewModel.loader.value == false) {
                            if (viewModel.selectedFolderUri.value.isNullOrBlank())
                                viewModel.showWarningWithDelay("Please select folder first")
                            else
                                viewModel.mCheckedState.value = it
                        }

                    }
                )
                Text(
                    text = "Use child folders too",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }

            val buttonModifier = Modifier
                .fillMaxWidth()
                .background(color = Color(0xFF756DF3), shape = RoundedCornerShape(size = 8.dp))

            val isButtonEnabled =
                viewModel.selectedFolderUri.value.isNullOrBlank() || viewModel.loader.value == true

            Row(
                modifier = if (isButtonEnabled) buttonModifier
                    .noRippleClickable { viewModel.showWarningWithDelay("Please select folder first") }
                    .background(color = Color(0x66FFFFFF), shape = RoundedCornerShape(size = 8.dp))
                    .padding(top = 16.dp, bottom = 16.dp)
                else
                    buttonModifier
                        .clickable {
                            storageReadLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                        }
                        .padding(top = 16.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "Classify to Folder",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(600),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }

            AnimatedVisibility(
                visible = viewModel.showWarning.value,
                enter = enterTransition,
                exit = exitTransition
            ) {
                Column(
                    Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (viewModel.loader.value == true) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = viewModel.warningText.value ?: "",
                            style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.60f))

        CreditButtons()
    }
}


@Composable
fun CreditButtons() {
    val context = LocalContext.current
    val viewModel = hiltViewModel<CreditButtonsViewModel>()

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {



        Row(
            modifier =  Modifier.clickable {
                viewModel.giveRating(context)
            }
                .fillMaxWidth()
                .background(color = Color(0xFF756DF3), shape = RoundedCornerShape(size = 8.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Give Us 5 Star",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF)
                )
            )
        }

        Row(
            modifier =  Modifier
                .clickable {
                    viewModel.shareApp(context)
                }
                .border(
                    width = 1.dp,
                    color = Color(0xFF756DF3),
                    shape = RoundedCornerShape(size = 8.dp)
                )
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = "Share This App!",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFF756DF3)
                )
            )
        }
    }
}
