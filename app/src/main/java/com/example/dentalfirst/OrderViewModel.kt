package com.example.dentalfirst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dentalfirst.utils.orderStateStub

class OrderViewModel : ViewModel() {

    var orderState: OrderState by mutableStateOf(orderStateStub)
        private set

    var bottomSheetShown: Boolean = false

    fun processIntent(intent: OrderIntent) {
        when (intent) {
            is OrderIntent.SelectFulfillmentType -> selectFulfillmentType(intent.type)
            is OrderIntent.SelectDeliveryItem -> selectDeliveryItem(intent.item)
            is OrderIntent.SelectCourierDate -> selectCourierDate(intent.date)
            OrderIntent.DeliveryFeeDismissedBottomSheet -> dismissBottomSheet()
            is OrderIntent.AddBonus -> addBonus(intent.amount)
            is OrderIntent.AddPromo -> addPromo(intent.text)
            OrderIntent.RemoveBonus -> removeBonus()
            OrderIntent.RemovePromo -> removePromo()
        }
    }

    private fun removePromo() {
        orderState = orderState.copy(appliedPromo = Promo.None)
    }

    private fun removeBonus() {
        orderState = orderState.copy(bonus = Bonus(0))
    }

    private fun addPromo(text: String) {
        val percentsDiscount = 10
        orderState = orderState.copy(appliedPromo = Promo(text, percentsDiscount),
            totalPrice = orderState.totalPrice * (100 - percentsDiscount) / 100
        )
    }

    private fun addBonus(amount: Int){
        orderState = orderState.copy(bonus = Bonus(amount),
            totalPrice = (orderState.totalPrice - amount * 100).coerceAtLeast(0)
        )
    }

    private fun dismissBottomSheet() {
        orderState = orderState.copy(showDeliveryFeeBottomSheet = false)
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
        if (item.type != DeliveryType.Courier && !bottomSheetShown) {
            orderState = orderState.copy(showDeliveryFeeBottomSheet = true)
            bottomSheetShown = true
        }
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
    object DeliveryFeeDismissedBottomSheet : OrderIntent
    data class AddPromo(val text: String) : OrderIntent
    object RemovePromo : OrderIntent
    data class AddBonus(val amount: Int): OrderIntent
    object RemoveBonus : OrderIntent
}