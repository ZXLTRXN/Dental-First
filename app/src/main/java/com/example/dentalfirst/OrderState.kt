package com.example.dentalfirst

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


@Stable
data class OrderState(
    val date: String,
    val id: Long,
    val customer: Customer,
    val items: Int,
    val totalPrice: Int,
    val appliedPromo: Promo = Promo.None,
    val bonus: Bonus = Bonus(0),

    val selectedFulfillmentType: FulfillmentType = FulfillmentType.DELIVERY,
    val deliveryAddress: FulfillmentAddress = FulfillmentAddress.Example,
    val pickupAddress: FulfillmentAddress = FulfillmentAddress.None,
    val deliveryPrice: Int = 0,

    val selectedPaymentType: PaymentType = PaymentType.INDIVIDUAL,
    val selectedIndividualPaymentType: IndividualPaymentType = IndividualPaymentType.CARD,
    val selectedLegalPaymentType: LegalPaymentInfo = LegalPaymentInfo.None,

    val deliveryItems: List<DeliveryItem> = DeliveryItem.Example

) {
//    var _items by mutableIntStateOf(0)
//    val items: Int get() = _items
//    var _totalPrice by mutableIntStateOf(0)
//    val totalPrice: Int get() = _totalPrice
//    var _appliedPromo by mutableStateOf(Promo.None)
//    val appliedPromo get() = _appliedPromo
//    var _bonus by mutableStateOf(Bonus(0))
//    val bonus get() = _bonus
//
//    var _selectedFulfillmentType by mutableStateOf(FulfillmentType.DELIVERY)
//    val selectedFulfillmentType get() = _selectedFulfillmentType
//    var _deliveryState by mutableStateOf(FulfillmentAddress.Example)
//    val deliveryState get() = _deliveryState
//    var _pickupState by mutableStateOf(FulfillmentAddress())
//    val pickupState get() = _pickupState
//    var _deliveryPrice by mutableIntStateOf(0)
//    val deliveryPrice: Int get() = _deliveryPrice
//
//    var _selectedPaymentType by mutableStateOf(PaymentType.INDIVIDUAL)
//    val selectedPaymentType get() = _selectedPaymentType
//
//    var _selectedIndividualPaymentType by mutableStateOf(IndividualPaymentType.CARD)
//    val selectedIndividualPaymentType get() = _selectedIndividualPaymentType
//

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

sealed class DeliveryType(
    val stringRes: Int,
    val iconRes: Int
) {
    object Courier : DeliveryType(
        R.string.delivery_courier,
        R.drawable.courier_ic
    )

    object EMS : DeliveryType(
        R.string.delivery_ems,
        R.drawable.ems_ic
    )

    object BusinessLines : DeliveryType(
        R.string.delivery_business_lines,
        R.drawable.business_lines_ic
    )

    object Mail : DeliveryType(
        R.string.delivery_russia_mail,
        R.drawable.russian_post_ic
    )

    object CDEK : DeliveryType(
        R.string.delivery_sdek,
        R.drawable.cdek_ic
    )

    object PEK : DeliveryType(
        R.string.delivery_pek,
        R.drawable.pec_ic
    )
}

@Stable
data class DeliveryItem(
    val type: DeliveryType,
    val isSelected: MutableState<Boolean> = mutableStateOf(false),
    val isEnabled: MutableState<Boolean> = mutableStateOf(true)
) {

    companion object {
        val Example = listOf(
            DeliveryItem(
                DeliveryType.Courier,
                mutableStateOf(true),
            ),
            DeliveryItem(
                DeliveryType.EMS,
            ),
            DeliveryItem(
                DeliveryType.Mail,
            ),
            DeliveryItem(
                DeliveryType.CDEK,
            ),
            DeliveryItem(
                DeliveryType.BusinessLines,
            ),
            DeliveryItem(
                DeliveryType.PEK,
            )
        )
    }
}

fun List<DeliveryItem>.select(item: DeliveryItem) { // fixme check
    forEach {
        it.isSelected.value = false
    }
    item.isSelected.value = true
}


enum class DestinationType(val stringRes: Int) {
    MOSCOW(R.string.delivery_moscow),
    NEAR_MOSCOW(R.string.delivery_close_moscow_region),
    RUSSIA(R.string.delivery_russia),
    INTERNATIONAL(R.string.delivery_abroad)
}

data class FulfillmentAddress(
    val country: String = "",
    val city: String = "",
    val address: String = "",
    val destinationType: DestinationType = DestinationType.MOSCOW
) {
    companion object {
        val None = FulfillmentAddress()
        val Example = FulfillmentAddress(
            "Россия",
            "Москва",
            "ул. Большая Ордынка 1, кв. 148, 605068",
            DestinationType.MOSCOW
        )
    }
}

enum class PaymentType(val stringRes: Int) {
    INDIVIDUAL(R.string.individual_entity_payment),
    LEGAL(R.string.legal_entity_payment)
}

enum class IndividualPaymentType(
    val stringRes: Int,
    val iconRes: Int
) {
    CARD(
        R.string.individual_card_payment,
        R.drawable.card_ic
    ),
    CASH(
        R.string.individual_cash_payment,
        R.drawable.wallet_money_ic
    )
}

data class LegalPaymentInfo(val inn: String) {
    companion object {
        val None = LegalPaymentInfo("")
    }
}
