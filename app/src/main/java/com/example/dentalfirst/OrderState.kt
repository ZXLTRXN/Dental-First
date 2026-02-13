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

    var _selectedFulfillmentType by mutableStateOf(FulfillmentType.DELIVERY)
    val selectedFulfillmentType get() = _selectedFulfillmentType
    var _deliveryState by mutableStateOf(FulfillmentAddress())
    val deliveryState get() = _deliveryState
    var _pickupState by mutableStateOf(FulfillmentAddress())
    val pickupState get() = _pickupState
    var _deliveryPrice by mutableIntStateOf(0)
    val deliveryPrice: Int get() = _deliveryPrice

    var _selectedPaymentType by mutableStateOf(PaymentType.INDIVIDUAL)
    val selectedPaymentType get() = _selectedPaymentType

    var _selectedIndividualPaymentType by mutableStateOf(IndividualPaymentType.CARD)
    val selectedIndividualPaymentType get() = _selectedIndividualPaymentType


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

enum class FulfillmentType(val stringRes: Int) {
    DELIVERY(R.string.delivery),
    PICKUP(R.string.pickup)
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

enum class PaymentType(val stringRes: Int) {
    INDIVIDUAL(R.string.individual_entity_payment),
    LEGAL(R.string.legal_entity_payment)
}

enum class IndividualPaymentType(val stringRes: Int, val iconRes: Int) {
    CARD(R.string.individual_card_payment, R.drawable.card_ic),
    CASH(R.string.individual_cash_payment, R.drawable.wallet_money_ic)
}
