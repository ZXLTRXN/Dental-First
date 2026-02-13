package com.example.dentalfirst

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
    var _appliedPromo by mutableStateOf(Promo.None)
    val appliedPromo get() = _appliedPromo
    var _bonus by mutableStateOf(Bonus(0))
    val bonus get() = _bonus

    var _selectedType by mutableStateOf(FulfillmentType.DELIVERY)
    val selectedType get() = _selectedType
    var _deliveryState by mutableStateOf(FulfillmentAddress())
    val deliveryState get() = _deliveryState
    var _pickupState by mutableStateOf(FulfillmentAddress())
    val pickupState get() = _pickupState
    var _deliveryPrice by mutableIntStateOf(0)
    val deliveryPrice: Int get() = _deliveryPrice
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
        val None = Promo(
            "",
            0f
        )
        val Example = Promo(
            "DFSALE",
            0f
        )
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
