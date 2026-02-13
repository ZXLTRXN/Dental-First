package com.example.dentalfirst.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dentalfirst.IndividualPaymentType
import com.example.dentalfirst.PaymentType
import com.example.dentalfirst.ui.theme.DarkGrey
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import com.example.dentalfirst.ui.theme.Purple
import com.example.dentalfirst.ui.theme.SuperLightGrey
import com.example.dentalfirst.ui.theme.TooLightGrey

////// PAYMENT TYPE
@Composable
fun PaymentTypeSelector(
    selectedType: PaymentType,
    onSelect: (PaymentType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = remember { PaymentType.entries }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { type ->
            PaymentOptionItem(
                type = type,
                isSelected = selectedType == type,
                onClick = { onSelect(type) }
            )
        }
    }
}

@Composable
private fun PaymentOptionItem(
    type: PaymentType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) Purple else SuperLightGrey,
        label = "bg_anim"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else DarkGrey,
        label = "content_color_anim"
    )

    Button(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            CustomRadioIndicator(
                selected = isSelected,
                color = contentColor
            )

            Text(
                text = stringResource(type.stringRes),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun CustomRadioIndicator(
    selected: Boolean,
    color: Color
) {
    val scale by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        label = "scale_anim"
    )

    Box(
        modifier = Modifier
            .size(16.dp)
            .border(
                width = 1.dp,
                color = if (selected) color else DarkGrey,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.7f)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .background(
                    color = color,
                    shape = CircleShape
                )
        )
    }
}

@Preview
@Composable
private fun PreviewPaymentTypeSelector() {
    DentalFirstTheme() {
        var selected by remember { mutableStateOf(PaymentType.INDIVIDUAL) }

        PaymentTypeSelector(
            selectedType = selected,
            onSelect = { selected = it }
        )
    }
}

//// INDIVIDUAL PAYMENT TYPE
@Composable
fun IndividualPaymentTypeSelector(
    selectedType: IndividualPaymentType,
    onSelect: (IndividualPaymentType) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = remember { IndividualPaymentType.entries }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        options.forEach { type ->
            IndividualPaymentOptionItem(
                type = type,
                isSelected = selectedType == type,
                onClick = { onSelect(type) }
            )
        }
    }
}

@Composable
private fun IndividualPaymentOptionItem(
    type: IndividualPaymentType,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) Purple else SuperLightGrey,
        label = "bg_anim"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else DarkGrey,
        label = "content_color_anim"
    )

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.primary else TooLightGrey,
        label = "border_color_anim"
    )

    OutlinedButton(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(1.dp, color = borderColor),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(ImageVector.vectorResource(type.iconRes), contentDescription = null)

            Text(
                text = stringResource(type.stringRes),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Preview
@Composable
private fun PreviewIndividualPaymentTypeSelector() {
    DentalFirstTheme() {
        var selected by remember { mutableStateOf(IndividualPaymentType.CARD) }

        IndividualPaymentTypeSelector(
            selectedType = selected,
            onSelect = { selected = it }
        )
    }
}