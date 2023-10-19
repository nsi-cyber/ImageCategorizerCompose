package com.nsicyber.imagecategorizer.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.nsicyber.imagecategorizer.models.ClassifiedFolderModel
import com.nsicyber.imagecategorizer.models.FolderViewModel
import com.nsicyber.imagecategorizer.models.ImageViewModel
import com.nsicyber.imagecategorizer.viewModels.ChooseDirectoryViewModel

@Composable
fun ClassifiedFolderDetailScreen(
    navHostController: NavHostController,
    result: ClassifiedFolderModel?
) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Images",
            style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFFFFFFFF),
            )
        )


        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxSize()
        ) {


            LazyVerticalGrid(modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                userScrollEnabled = true,


                content = {
                    items(result?.images?.size ?: 0) { index ->
                        Box(Modifier.padding(4.dp)) {
                            ImageView(model = ImageViewModel(uri = result?.images?.get(index)))
                        }
                    }
                })


        }


    }


}