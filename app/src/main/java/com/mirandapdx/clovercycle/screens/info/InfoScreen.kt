package com.mirandapdx.clovercycle.screens.info

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mirandapdx.clovercycle.ui.theme.PurpleGrey40

@Composable
fun InfoScreen(modifier: Modifier) {
    val viewModel = remember { InfoScreenViewModel() }
    Column((modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        viewModel.infoBoxes.forEach { infoItem ->
            InfoBox {
                infoItem.forEach { contents ->
                    Text(contents)
                }
            }
        }
    }
}

@Composable
fun InfoBox(content: @Composable (() -> Unit)) {
    Box(Modifier.fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .border(
            width = 1.dp,
            color = PurpleGrey40,
            shape = RoundedCornerShape(12.dp))
        .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp))
    {
        Column {
            content.invoke()
        }
    }
}