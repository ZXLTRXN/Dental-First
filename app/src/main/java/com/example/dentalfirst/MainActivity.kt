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
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dentalfirst.ui.theme.DentalFirstTheme
import androidx.lifecycle.viewmodel.compose.viewModel

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
    Column(modifier = modifier) {

    }

}

@Composable
fun OrderHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.size(33.dp),
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
    Column(modifier = modifier) {
        Row {
            Text(
                text = "Заказ #${orderState.id}",
                style = MaterialTheme.typography.titleLarge
            )
            Text(text = orderState.date)
        }

    }
}



@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    DentalFirstTheme {
        OrderScreen(orderStateStub)
    }
}