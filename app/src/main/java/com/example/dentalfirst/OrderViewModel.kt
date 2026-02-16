package com.example.dentalfirst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.dentalfirst.models.Bonus
import com.example.dentalfirst.models.DeliveryItem
import com.example.dentalfirst.models.DeliveryType
import com.example.dentalfirst.models.DestinationType
import com.example.dentalfirst.models.FulfillmentAddress
import com.example.dentalfirst.models.FulfillmentType
import com.example.dentalfirst.models.OrderState
import com.example.dentalfirst.models.Promo
import com.example.dentalfirst.models.select
import com.example.dentalfirst.utils.orderStateStub

class OrderViewModel(
) : ViewModel() {

    var orderState: OrderState by mutableStateOf(orderStateStub)
        private set

    var bottomSheetShown: Boolean = false

    private fun updateAddressInState(address: FulfillmentAddress) {
        orderState = orderState.copy(
            deliveryAddress = address,
        )
        updateDatesSelectorVisibility()
    }

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
            is OrderIntent.AddItem -> addOrderItem(intent.id)
            is OrderIntent.RemoveItem -> removeOrderItem(intent.id)

            OrderIntent.OpenAddressSelection -> {}
            is OrderIntent.OpenMapSelection -> {}

            is OrderIntent.UpdateAddress -> updateAddressInState(intent.address)
        }
    }

    private fun removeOrderItem(id: String) {
        updateCount(id) { it - 1 }
    }

    private fun addOrderItem(id: String) {
        updateCount(id) { it + 1 }
    }


    private fun updateCount(
        itemId: String,
        transform: (Int) -> Int
    ) {
        orderState = orderState.copy(
            items = orderState.items
                .map { item ->
                    if (item.id == itemId) {
                        item.copy(count = transform(item.count))
                    } else item
                }
                .filter { it.count > 0 }
        )
    }

    private fun removePromo() {
        orderState = orderState.copy(appliedPromo = Promo.None)
    }

    private fun removeBonus() {
        orderState = orderState.copy(bonus = Bonus(0))
    }

    private fun addPromo(text: String) {
        val percentsDiscount = 10
        orderState = orderState.copy(
            appliedPromo = Promo(
                text,
                percentsDiscount
            )
        )
    }

    private fun addBonus(amount: Int) {
        orderState = orderState.copy(bonus = Bonus(amount * 100))
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
        if (item.type != DeliveryType.Courier && !bottomSheetShown) { // fixme на getter
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
        orderState = orderState.copy(showDatesSelector = isMoscowCourier) // fixme на getter
    }

}

sealed interface OrderIntent {
    data class SelectFulfillmentType(val type: FulfillmentType) : OrderIntent
    data class SelectDeliveryItem(val item: DeliveryItem) : OrderIntent
    data class SelectCourierDate(val date: String) : OrderIntent
    object DeliveryFeeDismissedBottomSheet : OrderIntent

    data class AddPromo(val text: String) : OrderIntent
    object RemovePromo : OrderIntent
    data class AddBonus(val amount: Int) : OrderIntent
    object RemoveBonus : OrderIntent

    data class AddItem(val id: String) : OrderIntent
    data class RemoveItem(val id: String) : OrderIntent

    object OpenAddressSelection : OrderIntent
    data class OpenMapSelection(
        val lat: Double ,
        val long: Double
    ) : OrderIntent

    data class UpdateAddress(val address: FulfillmentAddress) : OrderIntent

}