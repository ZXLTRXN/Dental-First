package com.example.dentalfirst

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf

@Stable
data class OrderState(
    val date: String,
    val id: Long,
    val customer: Customer
) {
    val totalPrice: MutableState<Int> = mutableIntStateOf(0)
    val appliedPromo: MutableState<Promo> = mutableStateOf(Promo.None)
    val bonus: MutableState<Bonus> = mutableStateOf(Bonus(0f))

    val selectedType: MutableState<OrderType>
    = mutableStateOf(OrderType.DELIVERY)
    val deliveryState: MutableState<FulfillmentAddress>
    = mutableStateOf(FulfillmentAddress())
    val pickupState: MutableState<FulfillmentAddress>
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
    }
}

@JvmInline
value class Bonus(
    val amount: Float
)

enum class OrderType {
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
