package com.example.dentalfirst

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
data class OrderState(
    val date: String,
    val id: Long,
    val customer: Customer,

) {
    var _items by mutableIntStateOf(0)
    val items: Int get() = _items
    var _totalPrice by mutableIntStateOf(0)
    val totalPrice: Int get() = _totalPrice
    var _appliedPromo by mutableStateOf(Promo.Example)
    val appliedPromo get() = _appliedPromo
    var _bonus by mutableStateOf(Bonus(325))
    val bonus get() = _bonus

    val _selectedType: MutableState<FulfillmentType>
    = mutableStateOf(FulfillmentType.DELIVERY)
    val _deliveryState: MutableState<FulfillmentAddress>
    = mutableStateOf(FulfillmentAddress())
    val _pickupState: MutableState<FulfillmentAddress>
    = mutableStateOf(FulfillmentAddress())
}

data class Customer(
    val name: String,
    val phone: String,
    val photoId: Int
)

data class Promo(
    val name: String,
    val amount: Float
) {
    companion object {
        val None = Promo("", 0f)
        val Example = Promo("DFSALE", 0f)
    }
}

@JvmInline
value class Bonus(
    val amount: Int
)

enum class FulfillmentType {
    DELIVERY,
    PICKUP
}

enum class DestinationType {
    MOSCOW,
    NEAR_MOSCOW,
    RUSSIA,
    INTERNATIONAL
}

data class FulfillmentAddress(
    val country: String = "",
    val city: String = "",
    val address: String = "",
    val destinationType: DestinationType = DestinationType.MOSCOW
) {
    companion object {
        val NotSelected = FulfillmentAddress()
    }
}
