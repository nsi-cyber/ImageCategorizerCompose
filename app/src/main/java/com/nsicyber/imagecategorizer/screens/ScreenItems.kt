package com.nsicyber.imagecategorizer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.nsicyber.imagecategorizer.R
import com.nsicyber.imagecategorizer.models.FolderViewModel
import com.nsicyber.imagecategorizer.models.ImageViewModel


@Composable
fun FolderView(model: FolderViewModel, previewFolder: () -> Unit, onItemSelected: () -> Unit) {

    Row(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = if (model.isSelected == true) Color(0xFF756DF3)
                else Color(0xFF505050),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .width(358.dp)
            .height(53.dp)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
            .clickable {
                onItemSelected()}
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.clickable { onItemSelected() },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(colors = RadioButtonDefaults.colors(
                unselectedColor = Color.White, selectedColor = Color(0xFF756DF3)
            ), selected = model.isSelected==true, onClick = {onItemSelected() })

            Text(
                text = model.title ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                )
            )

            Text(
                text = "(" + model.imageList?.size.toString() + ")",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF),
                )
            )
        }

        Image(
            modifier = Modifier
                .padding(1.dp)
                .width(6.dp)
                .height(12.dp).clickable { previewFolder() },
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = "image description",
            contentScale = ContentScale.Fit
        )

    }
}


@Composable
fun ImageView(model: ImageViewModel) {

    Column(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFF756DF3),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .width(175.dp)
            .height(202.dp)
            .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
    ) {

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start,
        ) {


                AsyncImage(modifier = Modifier
                    .width(160.dp)
                    .height(160.dp)
                    .clip( shape = RoundedCornerShape(size = 8.dp)),
                    model = model?.uri,
                    contentDescription = "image description",
                    contentScale = ContentScale.Crop
                )



            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = model?.uri?.takeLast(7) ?: "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                    )
                )
                Text(
                    text = "2,9 MB",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                    )
                )

            }


        }


    }

}





