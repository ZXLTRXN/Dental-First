package com.example.dentalfirst

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dentalfirst.ui.theme.TooLightGrey
import com.example.dentalfirst.utils.BasicBeige
import com.example.dentalfirst.utils.orderStateStub

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DentalFirstTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OrderScreenStateful(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderScreenStateful(
    modifier: Modifier = Modifier,
    viewModel: OrderViewModel = viewModel()
) {
    OrderScreen(orderState = viewModel.orderState, modifier = modifier)
}

@Composable
fun OrderScreen(
    orderState: OrderState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        OrderHeader(onBackClick =  {}) // fixme
        Spacer(modifier = Modifier.height(12.dp))
        OrderDetails(orderState)
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
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
            onClick = onBackClick
        ) {
            Box(contentAlignment = Alignment.Center,
                ) {
                Icon(
                    ImageVector.vectorResource(R.drawable.left_arrow_ic),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Text(
            text = "Оформление заказа",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OrderHeaderPreview() {
    DentalFirstTheme {
        OrderHeader({})
    }
}

@Composable
fun OrderDetails(
    orderState: OrderState,
    modifier: Modifier = Modifier
) {
    Surface(modifier = modifier.fillMaxWidth(), shape = MaterialTheme.shapes.medium) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(),
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
                    Text(text = orderState.date,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    Icon(
                        ImageVector.vectorResource(R.drawable.calendar_ic),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "• ${orderState.items} товаров",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Text(text = "• ${orderState.totalPrice} ₽",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondary
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
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Скидка:",
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



@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    DentalFirstTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            OrderScreen(
                orderStateStub,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}