package com.example.dentalfirst.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.dentalfirst.R

@Composable
fun BasicBeige(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    iconRes: Int = R.drawable.add_circle_ic,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = backgroundColor,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = 9.dp,
                horizontal = 8.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor,
            )
            Icon(
                ImageVector.vectorResource(iconRes),
                contentDescription = null,
                tint = contentColor
            )
        }

    }
}