package com.example.dentalfirst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dentalfirst.utils.orderStateStub

class OrderViewModel : ViewModel() {

    var orderState: OrderState by mutableStateOf(orderStateStub)
        private set

    fun processIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.SelectFulfillmentType -> selectFulfillmentType(intent.type)
            is OrderIntent.SelectDeliveryItem -> selectDeliveryItem(intent.item)
        }
    }

    private fun selectFulfillmentType(type: FulfillmentType) {
        orderState = orderState.copy(selectedFulfillmentType = type)
    }

    private fun selectDeliveryItem(item: DeliveryItem) {
        orderState.deliveryItems.select(item)
    }


}

sealed interface OrderIntent {
    data class SelectFulfillmentType(val type: FulfillmentType) : OrderIntent
    data class SelectDeliveryItem(val item: DeliveryItem) : OrderIntent
}