package com.example.dentalfirst

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceBetween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dentalfirst.components.BasicBeige
import com.example.dentalfirst.components.BulletText
import com.example.dentalfirst.components.CourierDateItem
import com.example.dentalfirst.components.DeliveryBottomSheet
import com.example.dentalfirst.components.FulfillmentTypeTabSelector
import com.example.dentalfirst.components.IndividualPaymentTypeSelector
import com.example.dentalfirst.components.InputBottomSheet
import com.example.dentalfirst.components.PaymentTypeSelector
import com.example.dentalfirst.components.PrimaryButton
import com.example.dentalfirst.components.TransportCompaniesSelector
import com.example.dentalfirst.ui.theme.DarkGrey
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import com.example.dentalfirst.ui.theme.Green
import com.example.dentalfirst.ui.theme.LightGrey
import com.example.dentalfirst.ui.theme.MiddleGrey
import com.example.dentalfirst.ui.theme.Orange
import com.example.dentalfirst.ui.theme.Orange_10
import com.example.dentalfirst.ui.theme.Purple
import com.example.dentalfirst.ui.theme.SuperLightGrey
import com.example.dentalfirst.ui.theme.TooLightGrey
import com.example.dentalfirst.utils.orderStateStub
import com.example.dentalfirst.utils.toPriceString

@Composable
fun OrderScreenStateful(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = viewModel()
) {
    OrderScreen(
        orderState = viewModel.orderState,
        processIntent = viewModel::processIntent,
        modifier = modifier
    )
}

@Composable
fun OrderScreen(
    orderState: OrderState,
    processIntent: (OrderIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    DeliveryBottomSheet(
        show = orderState.showDeliveryFeeBottomSheet,
        addAmountToFree = 35000, // fixme цена
        onDismiss = {
            processIntent(OrderIntent.DeliveryFeeDismissedBottomSheet)
        })

    var promoText by remember { mutableStateOf("") }
    var showPromoSheet by remember { mutableStateOf(false) }

    InputBottomSheet(
        text = promoText,
        showSheet = showPromoSheet,
        onValueChange = { promoText = it },
        onDismiss = { showPromoSheet = false },
        onAccept = {
            showPromoSheet = false // fixme
            promoText = ""
            processIntent(OrderIntent.AddPromo(promoText))
        },
        placeholder = "Введите купон",
        keyboardType = KeyboardType.Text
    )

    var bonusText by remember { mutableStateOf("") }
    var showBonusSheet by remember { mutableStateOf(false) }

    InputBottomSheet(
        text = bonusText,
        showSheet = showBonusSheet,
        onValueChange = { bonusText = it },
        onDismiss = { showBonusSheet = false },
        onAccept = {
            bonusText.toIntOrNull()?.let {
                showBonusSheet = false
                processIntent(OrderIntent.AddBonus(it))
                bonusText = ""
            }
        },
        placeholder = "Введите количество",
        keyboardType = KeyboardType.Number
    ) {
        Text(
            "Доступно ${orderState.userBonuses.amountRub} бонусов",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp
            ),
            color = Green
        )
    }

    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(MaterialTheme.colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        OrderHeader(
            onBackClick = {},
            modifier = Modifier.padding(horizontal = 20.dp)
        ) // fixme
        Spacer(modifier = Modifier.height(12.dp))
        OrderDetails(
            orderState,
            onAddPromoClick = {
                showPromoSheet = true
            },
            onAddBonusClick = {
                showBonusSheet = true
            },
            onRemovePromoClick = {
                processIntent(OrderIntent.RemovePromo)
            },
            onRemoveBonusClick = {
                processIntent(OrderIntent.RemoveBonus)
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        CustomerDetails(
            name = orderState.customer.name,
            phone = orderState.customer.phone,
            image = orderState.customer.photoId,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        FulfillmentTypeTabSelector(
            selectedType = orderState.selectedFulfillmentType,
            onTabSelected = {
                processIntent(OrderIntent.SelectFulfillmentType(it))
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        AnimatedContent(targetState = orderState.selectedFulfillmentType) { targetType ->
            if (targetType == FulfillmentType.DELIVERY) {
                if (orderState.deliveryAddress == FulfillmentAddress.None) {
                    DeliverySelection(
                        onMapClick = {}, // fixme
                        onManualClick = {}, // fixme
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                } else {
                    Column() {
                        DeliveryDetails(
                            orderState = orderState,
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        TransportationSelection(
                            orderState = orderState,
                            onCompanySelected = {
                                processIntent(OrderIntent.SelectDeliveryItem(it))
                            },
                            onCourierDateSelected = {
                                processIntent(OrderIntent.SelectCourierDate(it))
                            },
                            modifier = Modifier.padding(horizontal = 20.dp)
                        )
                    }

                }
            } else {
                PickupDetails(
                    onMapClick = {}, // fixme
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        PaymentDetails(
            orderState,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))
        TotalDetails(
            price = orderState.totalPrice,
            deliveryPrice = orderState.deliveryPrice,
            isButtonEnabled = true, // fixme
            onClick = {}, // fixme
            modifier = Modifier
        )

    }

}

@Composable
fun OrderHeader(
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
            text = "Оформление заказа",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun OrderDetails(
    orderState: OrderState,
    onAddPromoClick: () -> Unit,
    onAddBonusClick: () -> Unit,
    onRemovePromoClick: () -> Unit,
    onRemoveBonusClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(vertical = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Заказ #${orderState.id}",
                    style = MaterialTheme.typography.titleLarge,
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = orderState.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MiddleGrey
                    )
                    Icon(
                        ImageVector.vectorResource(R.drawable.calendar_ic),
                        contentDescription = null,
                        tint = MiddleGrey
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "• ${orderState.items} товаров",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MiddleGrey
                )
                Text(
                    text = "• ${orderState.totalPrice.toPriceString()} ₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MiddleGrey
                )
            }
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .background(TooLightGrey)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    "Скидка:",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.LightGray
                )
                AnimatedContent(orderState.appliedPromo) { promo ->
                    if (promo == Promo.None) {
                        BasicBeige(
                            text = "Купон",
                            onClick = onAddPromoClick,
                            contentColor = LightGrey,
                            backgroundColor = SuperLightGrey,
                        )
                    } else {
                        BasicBeige(
                            text = orderState.appliedPromo.name,
                            onClick = onRemovePromoClick,
                            iconRes = R.drawable.cross_ic,
                            contentColor = Green,
                            backgroundColor = Color(0xFFE7FCED),
                        )
                    }
                }
                AnimatedContent(orderState.bonus) { bonus ->
                    if (bonus == Bonus(0)) {
                        BasicBeige(
                            text = "Бонусы",
                            onClick = onAddBonusClick,
                            contentColor = LightGrey,
                            backgroundColor = SuperLightGrey,
                        )
                    } else {
                        BasicBeige(
                            text = "- ${orderState.bonus.amountRub} бонусов",
                            onClick = onRemoveBonusClick,
                            iconRes = R.drawable.cross_ic,
                            contentColor = Orange,
                            backgroundColor = Orange_10,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CustomerDetails(
    name: String,
    phone: String,
    image: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        name,
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        phone,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MiddleGrey
                    )
                }
            }

            Text(
                "Заказчик",
                style = MaterialTheme.typography.bodyLarge,
                color = LightGrey
            )
        }
    }
}

@Composable
fun TotalDetails(
    price: Int,
    deliveryPrice: Int,
    isButtonEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Стоимость доставки:",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MiddleGrey
                )
                Text(
                    if (deliveryPrice == 0) "Не рассчитано" else deliveryPrice.toPriceString()
                            + " ₽",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = LightGrey
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Итого:",
                    style = MaterialTheme.typography.titleLarge,
                    color = MiddleGrey
                )
                Text(
                    "${price.toPriceString()} ₽",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            PrimaryButton(
                text = "Оформить заказ",
                onClick = onClick,
                enabled = isButtonEnabled
            )
        }
    }
}

@Composable
fun MapButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonContent: @Composable RowScope.() -> Unit
) {
    Surface(
        onClick = {},
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.map_image),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
            )
            Button(
                onClick = onClick,
                shape = MaterialTheme.shapes.extraLarge,
                contentPadding = PaddingValues(
                    vertical = 8.dp,
                    horizontal = 16.dp
                ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    content = buttonContent
                )
            }
        }
    }
}

@Composable
fun DeliverySelection(
    onMapClick: () -> Unit,
    onManualClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(R.string.delivery_params),
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(12.dp))
            MapButton(
                onMapClick,
                modifier = Modifier.height(126.dp)
            ) {
                Text(
                    text = "Указать адрес на карте",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.add_circle_ic),
                    contentDescription = null
                )
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onManualClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(
                    16.dp
                ),
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                )
            ) {
                Text(
                    text = "Указать вручную",
                    style = MaterialTheme.typography.titleSmall.copy
                        (fontSize = 17.sp)
                )
            }
        }
    }
}

@Composable
fun DeliveryDetails(
    orderState: OrderState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.delivery_params),
                    style = MaterialTheme.typography.titleSmall
                )
                Icon(
                    ImageVector.vectorResource(R.drawable.pen_ic),
                    contentDescription = null
                )
            }

            Spacer(Modifier.height(16.dp))
            Text(
                "${orderState.deliveryAddress.country} ${orderState.deliveryAddress.city}",
                style =
                    MaterialTheme.typography.bodyLarge,
                color = MiddleGrey
            )
            Text(
                orderState.deliveryAddress.address,
                style = MaterialTheme.typography.bodyLarge
                    .copy(fontWeight = FontWeight.Medium)
            )
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .height(1.dp)
                    .background(TooLightGrey)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var switchState by remember { mutableStateOf(true) }
                Switch(
                    switchState,
                    { switchState = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.colorScheme.primary,
                        checkedTrackColor = Purple
                    )
                )
                Text(
                    "Сохранить этот адрес для оформления заказов",
                    color = MiddleGrey,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun TransportationSelection(
    orderState: OrderState,
    onCompanySelected: (DeliveryItem) -> Unit,
    onCourierDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.transportation_selection),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(orderState.deliveryAddress.destinationType.stringRes),
                style = MaterialTheme.typography.bodyLarge,
                color = Orange,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(Modifier.height(12.dp))
            TransportCompaniesSelector(
                list = orderState.deliveryItems,
                onSelect = { selected ->
                    onCompanySelected(selected)
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )


            AnimatedVisibility(orderState.showDatesSelector) {
                Column() {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "Желаемый день доставки",
                        style = MaterialTheme.typography.bodyLarge,
                        color = DarkGrey,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(orderState.courierDates) {
                            CourierDateItem(
                                text = it,
                                isSelected = it == orderState.courierDates[orderState.selectedCourierDateIdx],
                                onClick = {
                                    onCourierDateSelected(it)
                                }
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            DeliveryDescriptionMsk(Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
fun DeliveryDescriptionMsk(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.delivery_moscow_detailed),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 14.sp,
                lineHeight = 21.sp
            ),
            color = DarkGrey,
            modifier = Modifier
        )
        Spacer(Modifier.height(4.dp))
        BulletText(
            text = stringResource(R.string.delivery_moscow_detailed_1_bul),
            modifier = Modifier
        )
        Spacer(Modifier.height(4.dp))
        BulletText(
            text = stringResource(R.string.delivery_moscow_detailed_2_bul),
            modifier = Modifier
        )
        Spacer(Modifier.height(4.dp))
        BulletText(
            text = stringResource(R.string.delivery_moscow_detailed_3_bul),
            modifier = Modifier
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.delivery_moscow_detailed_4),
            style = MaterialTheme.typography.bodySmall,
            color = DarkGrey
        )
    }
}

@Composable
fun PickupDetails( // fixme
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Адрес офиса Dental First",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(12.dp))
            MapButton(
                onMapClick,
                modifier = Modifier.height(126.dp)
            ) {
                Text(
                    text = "Показать на карте",
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.square_arrow_right_ic),
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun PaymentDetails(
    orderState: OrderState,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Параметры оплаты",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(12.dp))

            var selectedPaymentType by remember { mutableStateOf(PaymentType.INDIVIDUAL) } // fixme
            PaymentTypeSelector(
                selectedType = selectedPaymentType,
                onSelect = { selectedPaymentType = it }
            )
            Spacer(Modifier.height(12.dp))

            Text(
                text = "Способы оплаты",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(12.dp))

            AnimatedContent(selectedPaymentType) { targetType ->
                when {
                    targetType == PaymentType.INDIVIDUAL -> {
                        var selectedIndividualPaymentType by remember { mutableStateOf(IndividualPaymentType.CARD) } // fixme
                        IndividualPaymentTypeSelector(
                            selectedType = selectedIndividualPaymentType,
                            onSelect = { selectedIndividualPaymentType = it }
                        )
                    }

                    targetType == PaymentType.LEGAL -> {

                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                "После оформления заказа ожидайте ответа от менеджера." +
                        " После того, как заказ будет согласован, вам нужно будет уточнить реквизиты" +
                        " для перевода оплаты. После того, как заказ будет оплачен, ожидайте чек" +
                        " из Яндекс ОФД на вашу электронную почту. Если вам нужен оригинал чека, " +
                        "предупредите об этом заранее.",
                style = MaterialTheme.typography.bodySmall,
                color = DarkGrey
            )

        }
    }
}

@Preview(
    showBackground = true,
    backgroundColor = 0xFFF0EAE2
)
@Composable
fun ItemPreview() {
    DentalFirstTheme {
        TransportationSelection(
            orderState = orderStateStub,
            onCompanySelected = {},
            onCourierDateSelected = {},
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    DentalFirstTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            OrderScreen(
                orderStateStub,
                modifier = Modifier.padding(innerPadding),
                processIntent = {}
            )
        }
    }
}