package com.mirandapdx.portlandcycling.screens.info

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.mirandapdx.portlandcycling.ui.theme.PurpleGrey40

@Composable
fun InfoScreen(modifier: Modifier) {
    Column((modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)),
        verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoBox()
        {
            Text("Event info pulled from www.shift2bikes.org public calendar API")
        }
        InfoBox()
        {
            Text("Long press events to add to Saved screen")
        }
        InfoBox()
        {
            Text("This app is open source, view the source code or contribute at https://github.com/randy-miller/PortlandCycling")
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