package com.mindlab.mapboxtest.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Created by Alireza Nezami on 12/11/2021.
 */
@Composable
fun PrimaryButton(
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 5.dp
        ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor =
            if (enabled) MaterialTheme.colors.primary
            else MaterialTheme.colors.secondary
        ),
        shape = RoundedCornerShape(50)
    ) {
        ButtonTitle(
            modifier = Modifier
                .padding(vertical = 5.dp, horizontal = 8.dp),
            text = text,
            color =
            if (enabled) MaterialTheme.colors.onPrimary
            else MaterialTheme.colors.secondaryVariant,
        )
    }
}


