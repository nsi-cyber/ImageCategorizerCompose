package com.nsicyber.imagecategorizer.screens

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.nsicyber.imagecategorizer.models.ClassifiedFolderList
import com.nsicyber.imagecategorizer.models.ClassifiedFolderModel
import com.nsicyber.imagecategorizer.models.FolderViewModel
import com.nsicyber.imagecategorizer.utils.getRealPathFromURI
import com.nsicyber.imagecategorizer.utils.toJson
import com.nsicyber.imagecategorizer.viewModels.ClassifiedFolderViewModel


@Composable
fun ClassifiedFoldersScreen(navHostController: NavHostController?, result: ClassifiedFolderList?) {
    val viewModel = hiltViewModel<ClassifiedFolderViewModel>()
    var mSelectAll by remember {
        mutableStateOf<Boolean>(false)
    }

    var launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                viewModel.copyImagesToFolder(
                    viewModel.classifiedFolderListObj.value?.list?.filter { item -> item?.isSelected == true },
                    getRealPathFromURI(uri)
                )
            }
        }

    LaunchedEffect(Unit) {
        if(viewModel.classifiedFolderListObj.value==null)
        viewModel.setupClassifiedResult(ClassifiedFolderList(result?.list?.filter { pre ->
            (pre?.images?.size?.minus(
                1
            ) != 0)
        }))
    }

    LaunchedEffect(viewModel.completed.value) {
        if (viewModel.completed.value)
            navHostController!!.navigate("success_screen")
    }


    Box(modifier = if (viewModel.showDialog.value == false) Modifier.fillMaxSize()
    else Modifier
        .fillMaxSize()
        .background(Color.Black.copy(alpha = 0.3f))
        .pointerInput(Unit) {}) {

        Column(
            modifier = Modifier
                .background(Color(0x40000000))
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp, 32.dp)
                .padding(bottom = 60.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = "Classified Files", style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(600),
                    color = Color(0xFFFFFFFF),
                )
            )



            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Folders", style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFFFFFFFF),
                        )
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            mSelectAll = !mSelectAll
                        }) {
                        Text(
                            text = "Select All", style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight(400),
                                color = Color(0xFFFFFFFF),
                            )
                        )



                        RadioButton(selected = mSelectAll, onClick = {
                            mSelectAll = !mSelectAll

                            viewModel.classifiedFolderListObj.value =
                                viewModel.classifiedFolderListObj.value?.copy(list = viewModel.classifiedFolderListObj.value?.list?.map {
                                    it?.copy(
                                        isSelected = mSelectAll
                                    )
                                })


                        })


                    }

                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),

                    ) {
                    repeat(
                        viewModel.classifiedFolderListObj.value?.list?.size ?: 0
                    ) { index ->

                        FolderView(model = FolderViewModel(
                            viewModel?.classifiedFolderListObj?.value?.list?.get(index)?.title,
                            viewModel?.classifiedFolderListObj?.value?.list?.get(index)?.images,
                            viewModel?.classifiedFolderListObj?.value?.list?.get(index)?.isSelected
                        ), previewFolder = {
                            navHostController!!.navigate(
                                "folder_screen_detail?result=${
                                    viewModel?.classifiedFolderListObj?.value?.list?.get(index)
                                        .toJson()
                                }"
                            )
                        }, onItemSelected = {
                            viewModel?.classifiedFolderListObj?.value =
                                viewModel?.classifiedFolderListObj?.value?.copy(list = viewModel?.classifiedFolderListObj?.value?.list?.toMutableList()
                                    ?.apply {
                                        this[index] =
                                            this[index]?.copy(isSelected = !this[index]!!.isSelected)
                                    })
                        })
                    }

                }


            }
        }


        if (viewModel.classifiedFolderListObj.value?.list?.filter { item -> item?.isSelected == true }
                ?.isEmpty() == false) {
            Column(
                Modifier
                    .shadow(
                        elevation = 10.dp,
                        spotColor = Color(0x40000000),
                        ambientColor = Color(0x40000000)
                    )
                    .fillMaxWidth()
                    .background(color = Color(0xFF27282E))
                    .padding(top = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomCenter),
                verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                    verticalAlignment = Alignment.Top,
                ) {
                    Row(
                        Modifier.clickable { navHostController!!.popBackStack() }
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                            ,
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp, Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = "Cancel", style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight(600),
                                color = Color(0xFFFFFFFF),
                            )
                        )
                    }


                    Row(
                        Modifier
                            .fillMaxWidth() .clickable {
                                launcher.launch(viewModel.openFolderPicker())
                            }
                            .background(
                                color = Color(0xFF756DF3), shape = RoundedCornerShape(size = 8.dp)
                            )
                            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                           ,
                        horizontalArrangement = Arrangement.spacedBy(
                            10.dp, Alignment.CenterHorizontally
                        ),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Text(
                            text = "Extract to Folder", style = TextStyle(
                                fontSize = 14.sp,
                                fontWeight = FontWeight(600),
                                color = Color(0xFFFFFFFF),
                            )
                        )
                    }

                }
            }
        }

        if (viewModel.showDialog.value) {

            Row(
                Modifier.align(Alignment.Center)
                    .fillMaxWidth()
                    .background(color = Color(0xFF3C3E45), shape = RoundedCornerShape(size = 12.dp))
                    .padding(start = 16.dp, top = 32.dp, end = 16.dp, bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.Start),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                )

                Text(
                    text = "Extracting images...",
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight(500),
                        color = Color(0xFFFFFFFF),
                    )
                )
            }


        }
    }
}
