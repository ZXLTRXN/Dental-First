package com.example.dentalfirst

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dentalfirst.components.BasicBeige
import com.example.dentalfirst.components.FulfillmentTypeTabSelector
import com.example.dentalfirst.components.PrimaryButton
import com.example.dentalfirst.components.SecondaryButton
import com.example.dentalfirst.components.TertiaryButton
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import com.example.dentalfirst.ui.theme.LightGrey
import com.example.dentalfirst.ui.theme.MiddleGrey
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
        onFulfillmentSelected = {
            viewModel.processIntent(OrderIntent.SelectFulfillmentType(it))
        },
        modifier = modifier
    )
}

@Composable
fun OrderScreen(
    orderState: OrderState,
    onFulfillmentSelected: (FulfillmentType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
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
            selectedType = orderState.selectedType,
            onTabSelected = onFulfillmentSelected,
            modifier = Modifier.padding(horizontal = 20.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (orderState.selectedType == FulfillmentType.DELIVERY) {
            DeliveryDetails(
                onMapClick = {}, // fixme
                onManualClick = {}, // fixme
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        } else {
            PickupDetails(
                onMapClick = {},
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        
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
                BasicBeige(
                    text = orderState.appliedPromo.name,
                    onClick = {}, // fixme
                    textColor = Color(0xFF0BDF47),
                    backgroundColor = Color(0xFFE7FCED),
                )
                BasicBeige(
                    text = "- ${orderState.bonus.amount} бонусов",
                    onClick = {}, // fixme
                    textColor = Color(0xFFFF8700),
                    backgroundColor = Color(0xFFFFF3E6),
                )
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
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = {},
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .height(126.dp)
            .fillMaxWidth(),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.map_image),
                contentDescription = null,
                contentScale = ContentScale.Crop
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
                    verticalAlignment = Alignment.CenterVertically
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
            }

        }

    }
}

@Composable
fun DeliveryDetails(
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
                text = "Параметры доставки",
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(Modifier.height(12.dp))
            MapButton(onMapClick)
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = onManualClick,
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(16.dp
                ),
                colors = ButtonDefaults.buttonColors().copy(
                    contentColor = MaterialTheme.colorScheme.onTertiary,
                    containerColor = MaterialTheme.colorScheme.tertiary,
                )
            ) {
                Text(text = "Указать вручную", style = MaterialTheme.typography.titleSmall.copy
                    (fontSize = 17.sp)
                )
            }
        }
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
        Column(modifier = Modifier.padding(vertical = 16.dp)) {

        }
    }
}

@Composable
fun PaymentDetails(modifier: Modifier = Modifier) {
    
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    DentalFirstTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            OrderScreen(
                orderStateStub,
                modifier = Modifier.padding(innerPadding),
                onFulfillmentSelected = {}
            )
        }
    }
}