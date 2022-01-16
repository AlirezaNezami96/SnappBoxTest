package com.mindlab.mapboxtest.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mindlab.mapboxtest.R

/**
 * Created by Alireza Nezami on 12/7/2021.
 */
@Composable
fun HeaderTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.onBackground,
) {
    Text(
        modifier = modifier.padding(vertical = 8.dp),
        text = text,
        style = MaterialTheme.typography.h5,
        fontWeight = FontWeight.Bold,
        color = color,
        textAlign = TextAlign.Center,
        fontFamily = FontFamily(
            Font(R.font.shabnam)
        )
    )
}

@Composable
fun SubtitleTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.secondaryVariant,
    fontWeight: FontWeight = FontWeight.Normal,
    textAlign: TextAlign = TextAlign.End
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = textAlign,
        style = MaterialTheme.typography.subtitle2,
        fontWeight = fontWeight,
        color = color,
        fontFamily = FontFamily(
            Font(R.font.shabnam)
        )
    )
}

@Composable
fun ButtonTitle(
    modifier: Modifier,
    text: String,
    color: Color = MaterialTheme.colors.onSecondary,
    fontWeight: FontWeight = FontWeight.Medium,
) {
    Text(
        modifier = modifier,
        text = text,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.button,
        fontWeight = FontWeight.Medium,
        color = color,
        fontFamily = FontFamily(
            Font(R.font.shabnam)
        )
    )
}
