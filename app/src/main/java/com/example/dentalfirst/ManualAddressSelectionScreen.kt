package com.example.dentalfirst

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dentalfirst.components.ScreenHeader
import com.example.dentalfirst.components.SecondaryButton
import com.example.dentalfirst.models.DestinationType
import com.example.dentalfirst.models.FulfillmentAddress
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import com.example.dentalfirst.ui.theme.LightGrey
import com.example.dentalfirst.ui.theme.MiddleGrey
import com.example.dentalfirst.ui.theme.SuperLightGrey
import com.example.dentalfirst.ui.theme.TooLightGrey

@Composable
fun ManualAddressSelectionScreen(
    onAddressSelected: (FulfillmentAddress) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        var city by remember { mutableStateOf("") }
        var streetHouse by remember { mutableStateOf("") }
        var flat by remember { mutableStateOf("") }
        var postal by remember { mutableStateOf("") }
        var selectedDestination by remember { mutableStateOf(DestinationType.MOSCOW) }


        val buttonEnabled by remember {
            derivedStateOf {
                streetHouse.isNotBlank() && flat.isNotBlank() && postal.isNotBlank()
            }
        }

        Column {
            Spacer(modifier = Modifier.height(12.dp))
            ScreenHeader(
                text = "Указать адрес доставки",
                onBackClick = { onBackClick() },
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            DestinationsSelector(
                selectedDestination = selectedDestination,
                onDestinationSelected = { selectedDestination = it })
            Spacer(modifier = Modifier.height(20.dp))
            AnimatedVisibility(selectedDestination != DestinationType.MOSCOW) {
                AddressTextField(
                    value = city,
                    onValueChange = { city = it },
                    placeholder = "Город",
                    keyboardType = KeyboardType.Text,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
                    showTrailingIcon = true
                )
            }

            AddressTextField(
                value = streetHouse,
                onValueChange = { streetHouse = it },
                placeholder = "Улица, дом",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            AddressTextField(
                value = flat,
                onValueChange = { flat = it },
                placeholder = "Квартира, офис",
                keyboardType = KeyboardType.Text,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            AddressTextField(
                value = postal,
                onValueChange = { postal = it },
                placeholder = "Индекс",
                keyboardType = KeyboardType.Number,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }


        SecondaryButton(
            "Добавить адрес",
            onClick = {
                onAddressSelected(FulfillmentAddress.Example)
            },
            enabled = buttonEnabled,
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 16.dp
            )
        )
    }
}

//@Composable
//fun AddressTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholder: String,
//    modifier: Modifier = Modifier,
//    keyboardType: KeyboardType = KeyboardType.Text
//) {
//    BasicTextField(
//        value = value,
//        onValueChange = onValueChange,
//        modifier = modifier.fillMaxWidth(),
//        textStyle = MaterialTheme.typography.bodySmall.copy(
//            fontSize = 16.sp,
//            color = Color.Black
//        ),
//        singleLine = true,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = keyboardType
//        ),
//        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
//        decorationBox = { innerTextField ->
//            Box(
//                modifier = Modifier
//                    .background(
//                        SuperLightGrey,
//                        shape = MaterialTheme.shapes.medium
//                    )
//                    .border(
//                        width = 1.dp,
//                        color = TooLightGrey,
//                        shape = MaterialTheme.shapes.medium
//                    )
//                    .padding(16.dp),
//                contentAlignment = Alignment.CenterStart
//            ) {
//                if (value.isEmpty()) {
//                    Text(
//                        text = placeholder,
//                        style = MaterialTheme.typography.bodySmall.copy(
//                            fontSize = 16.sp,
//                            color = MiddleGrey
//                        )
//                    )
//                }
//                innerTextField()
//            }
//        }
//    )
//}


@Composable
fun AddressTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    showTrailingIcon: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodySmall.copy(
            fontSize = 16.sp,
            color = Color.Black
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(
                        SuperLightGrey,
                        shape = MaterialTheme.shapes.medium
                    )
                    .border(
                        width = 1.dp,
                        color = TooLightGrey, // Убедитесь, что этот цвет определен
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Основная часть с текстом
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 16.sp,
                                    color = MiddleGrey
                                )
                            )
                        }
                        innerTextField()
                    }

                    // Иконка стрелочки
                    if (showTrailingIcon) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = MiddleGrey,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun DestinationsSelector(
    selectedDestination: DestinationType,
    onDestinationSelected: (DestinationType) -> Unit,
    modifier: Modifier = Modifier,
    destinations: List<DestinationType> = DestinationType.entries,
) {

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        items(destinations) {
            DestinationItem(
                text = stringResource(it.stringRes),
                onClick = {
                    onDestinationSelected(it)
                },
                isSelected = it == selectedDestination
            )
        }
    }

}

@Composable
fun DestinationItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.onTertiary else SuperLightGrey,
        label = "bg_anim"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) MaterialTheme.colorScheme.tertiary else MiddleGrey,
        label = "content_color_anim"
    )
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        contentPadding = PaddingValues(
            vertical = 12.dp,
            horizontal = 20.dp
        ),
        colors = ButtonDefaults.buttonColors().copy(
            contentColor = contentColor,
            containerColor = containerColor,
            disabledContentColor = LightGrey,
            disabledContainerColor = TooLightGrey
        )
    ) {
        Text(text)
    }

}

@Preview(showBackground = true)
@Composable
fun AddressPreview() {
    DentalFirstTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            ManualAddressSelectionScreen(
                onAddressSelected = {},
                onBackClick = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}