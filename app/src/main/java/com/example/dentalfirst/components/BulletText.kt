package com.example.dentalfirst.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import com.example.dentalfirst.ui.theme.DarkGrey

@Composable
fun BulletText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        buildAnnotatedString {
            append(" • ")
            append(text)
        },
        style = MaterialTheme.typography.bodySmall,
        color = DarkGrey,
        modifier = modifier
    )
}