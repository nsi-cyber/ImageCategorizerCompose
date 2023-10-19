package com.nsicyber.imagecategorizer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.nsicyber.imagecategorizer.R
import com.nsicyber.imagecategorizer.models.FolderViewModel
import com.nsicyber.imagecategorizer.models.ImageViewModel

@Composable
fun FolderView(model: FolderViewModel, previewFolder: () -> Unit, onItemSelected: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onItemSelected() }
            .border(
                width = 1.dp,
                color = if (model.isSelected == true) Color(0xFF756DF3) else Color(0xFF505050),
                shape = RoundedCornerShape(size = 8.dp)
            )
            .width(358.dp)
            .height(53.dp)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable { onItemSelected() },
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                colors = RadioButtonDefaults.colors(
                    unselectedColor = Color.White,
                    selectedColor = Color(0xFF756DF3)
                ),
                selected = model.isSelected == true,
                onClick = { onItemSelected() }
            )
            Text(
                text = model.title ?: "",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF)
                )
            )
            Text(
                text = "(${model.imageList?.size})",
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFFFFFFFF)
                )
            )
        }
        Image(
            modifier = Modifier.clickable { previewFolder() }
                .padding(1.dp)
                .size(6.dp, 12.dp),
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
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.Start
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(160.dp)
                    .clip(shape = RoundedCornerShape(size = 8.dp)),
                model = model.uri,
                contentDescription = "image description",
                contentScale = ContentScale.Crop
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = model.uri?.toUri()?.lastPathSegment ?: "",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF)
                    ),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}






