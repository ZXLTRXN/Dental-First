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
            is OrderIntent.SelectCourierDate -> selectCourierDate(intent.date)
        }
    }

    private fun selectCourierDate(date: String) {
        orderState.courierDates.indexOf(date).let { idx ->
            orderState = orderState.copy(selectedCourierDateIdx = idx)
        }
    }

    private fun selectFulfillmentType(type: FulfillmentType) {
        orderState = orderState.copy(selectedFulfillmentType = type)
        updateDatesSelectorVisibility()
    }

    private fun selectDeliveryItem(item: DeliveryItem) {
        orderState.deliveryItems.select(item) // рекомпозиций избыточных нет
        updateDatesSelectorVisibility()
    }

    private fun updateDatesSelectorVisibility() {
        val isMoscowCourier =
        orderState.selectedFulfillmentType == FulfillmentType.DELIVERY &&
                orderState.deliveryAddress.destinationType == DestinationType.MOSCOW &&
                orderState.deliveryItems.first { it.type == DeliveryType.Courier }.isSelected
                    .value
        orderState = orderState.copy(showDatesSelector = isMoscowCourier)
    }


}

sealed interface OrderIntent {
    data class SelectFulfillmentType(val type: FulfillmentType) : OrderIntent
    data class SelectDeliveryItem(val item: DeliveryItem) : OrderIntent
    data class SelectCourierDate(val date: String) : OrderIntent
}