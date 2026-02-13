package com.example.dentalfirst.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dentalfirst.FulfillmentType
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import com.example.dentalfirst.ui.theme.MiddleGrey
import com.example.dentalfirst.ui.theme.TooLightGrey


@Composable
fun FulfillmentTypeTabSelector(
    selectedType: FulfillmentType,
    onTabSelected: (FulfillmentType) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = FulfillmentType.entries
    val selectedIndex = selectedType.ordinal


    val indicatorOffset by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "TabSelection"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(TooLightGrey)
            .padding(4.dp)
    ) {
        // Рисуем подложку, которая занимает 1/N ширины
        // Мы используем layout-логику самого Row для позиционирования
//        Row(modifier = Modifier.fillMaxSize()) {
//            tabs.forEachIndexed { index, _ ->
//                Spacer(modifier = Modifier.weight(1f))
//            }
//        }

        // Индикатор (Белый фон)
        // Мы используем Modifier.fillMaxWidth(1f / tabs.size)
        // и смещаем его через fraction (долю) от общей ширины
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(1f / tabs.size)
                .graphicsLayer {
                    // Смещаем на ширину одного таба, умноженную на прогресс анимации
                    translationX = indicatorOffset * size.width
                }
                .background(
                    MaterialTheme.colorScheme.surface,
                    MaterialTheme.shapes.small
                )
        )

        // Текстовый слой
        Row(modifier = Modifier.fillMaxSize()) {
            tabs.forEachIndexed { index, type ->
                val isSelected = selectedIndex == index

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onTabSelected(type) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(type.stringRes),
                        color = animateColorAsState(if (isSelected) Color.Black else MiddleGrey).value,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight
                                .Light
                        ),
                        modifier = Modifier.padding(
                            horizontal = 4.dp,
                            vertical = 8.dp
                        )
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFulfillmentTypeTabSelector() {
    DentalFirstTheme() {
        var selectedType by remember { mutableStateOf(FulfillmentType.DELIVERY) }
        FulfillmentTypeTabSelector(
            selectedType = selectedType,
            onTabSelected = { selectedType = it }
        )
    }
}