package com.example.dentalfirst.navigation

import MapAddressSelectionScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dentalfirst.ManualAddressSelectionScreen
import com.example.dentalfirst.OrderIntent
import com.example.dentalfirst.OrderScreenStateful
import com.example.dentalfirst.OrderViewModel
import com.example.dentalfirst.models.FulfillmentAddress

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(0.dp)
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = OrderRoute
    ) {
        composable<OrderRoute> { backStackEntry ->
            val viewModel: OrderViewModel = viewModel()
            OrderScreenStateful(
                onNavigateToAddress = {
                    navController.navigate(AddressPickerRoute)
                },
                onNavigateToMap = {
                    navController.navigate(MapRoute)
                },
                viewModel = viewModel,
                modifier = modifier,
                innerPadding = innerPadding
            )
            val address = backStackEntry.savedStateHandle
                .getStateFlow<FulfillmentAddress?>(
                    "selected_address",
                    null
                )
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
            ManualAddressSelectionScreen(
                onAddressSelected = { address ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "selected_address",
                            address
                        )

                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
                modifier = modifier.padding(innerPadding)
            )
        }

        // Экран выбора адреса
        composable<MapRoute> {
            MapAddressSelectionScreen(
                onAddressSelected = { address ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "selected_address",
                            address
                        )

                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
                modifier = modifier.padding(innerPadding)
            )
        }
    }
}

