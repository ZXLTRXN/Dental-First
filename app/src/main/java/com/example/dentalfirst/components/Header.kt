package com.example.dentalfirst.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
fun ScreenHeader(
    text: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Surface(
            modifier = Modifier
                .padding(7.dp)
                .size(33.dp),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.2f),
            onClick = onBackClick
        ) {
            Box(
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.left_arrow_ic),
                    contentDescription = null,
                    tint = Color.Black
                )
            }
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}