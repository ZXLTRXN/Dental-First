package com.example.dentalfirst.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dentalfirst.OrderIntent
import com.example.dentalfirst.OrderScreenStateful
import com.example.dentalfirst.OrderViewModel
import com.example.dentalfirst.models.FulfillmentAddress

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OrderRoute // Используем объект вместо строки
    ) {
        // Экран заказов
        composable<OrderRoute> { backStackEntry ->
            val viewModel: OrderViewModel = viewModel()
            OrderScreenStateful(
                onNavigateToAddress = {
                    navController.navigate(AddressPickerRoute)
                },
                viewModel = viewModel
            )
            val address = backStackEntry.savedStateHandle
                .getStateFlow<FulfillmentAddress?>("selected_address", null)
                .collectAsState()

            LaunchedEffect(address.value) {
                address.value?.let {
                    viewModel.processIntent(OrderIntent.UpdateAddress(it))
                    backStackEntry.savedStateHandle["selected_address"] = null
                }
            }
        }

        // Экран выбора адреса
        composable<AddressPickerRoute> {
            AddressPickerScreen(
                onAddressSelected = { address ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_address", address)

                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
fun AddressPickerScreen(onAddressSelected: (FulfillmentAddress) -> Unit) {
    Column(Modifier.fillMaxSize()) {
        Text("AAAAAA")
        Button(onClick = {
            onAddressSelected(FulfillmentAddress.Example)
        }) {
            Text("AAAAAA")
        }

    }
}