package com.example.dentalfirst.models

import androidx.compose.runtime.Immutable
import com.example.dentalfirst.R


@Immutable
data class OrderState(
    val date: String,
    val id: Long,
    val customer: Customer,
    val items: List<OrderItem>,
    val appliedPromo: Promo = Promo.None,

    val userBonuses: Bonus = Bonus(0),
    val bonus: Bonus = Bonus(0),

    val selectedFulfillmentType: FulfillmentType = FulfillmentType.DELIVERY,
    val deliveryAddress: FulfillmentAddress = FulfillmentAddress.None,
    val pickupAddress: FulfillmentAddress = FulfillmentAddress.Company,

    val selectedPaymentType: PaymentType = PaymentType.INDIVIDUAL,
    val selectedIndividualPaymentType: IndividualPaymentType = IndividualPaymentType.CARD,
    val selectedLegalPaymentType: LegalPaymentInfo = LegalPaymentInfo.None,

    val deliveryItems: List<DeliveryItem> = DeliveryItem.Example,
    val courierDates: List<String> = emptyList(),
    val selectedCourierDateIdx: Int = 0,
    val showDatesSelector: Boolean = false,
    val showDeliveryFeeBottomSheet: Boolean = false,
) {
    val basePrice: Int
        get() {
            return items.sumOf { it.basePrice }
        }

    val discountedPrice: Int
        get() = items.sumOf { it.getDiscountedPrice(appliedPromo) }

    val selectedDeliveryType: DeliveryType
        get() = deliveryItems.first { it.isSelected.value }.type


    val deliveryPrice: Int
        get() {
            return when (selectedFulfillmentType) {
                FulfillmentType.DELIVERY -> {
                    if (selectedDeliveryType == DeliveryType.Courier) {
                        100000
                    } else {
                        150000
                    }
                }

                FulfillmentType.PICKUP -> {
                    175000
                }
            }
        }


    val deliveryPriceCounted: Boolean
        get() =
            deliveryAddress != FulfillmentAddress.None ||
                    pickupAddress != FulfillmentAddress.None


    val totalPriceWithoutDelivery: Int
        get() {
            return (discountedPrice - bonus.amount).coerceAtLeast(0)
        }

    val totalPrice: Int
        get() {
            return (discountedPrice + deliveryPrice - bonus.amount).coerceAtLeast(0)
        }

    val itemsCount: Int
        get() {
            return items.sumOf { it.count }
        }

    val paymentAvailable: Boolean
        get() = (deliveryAddress != FulfillmentAddress.None ||
                pickupAddress != FulfillmentAddress.None) &&
                selectedPaymentType == PaymentType.INDIVIDUAL

}

data class Customer(
    val name: String,
    val phone: String,
    val photoId: Int
)

data class Promo(
    val name: String,
    val amountPercents: Int
) {
    companion object {
        val None = Promo(
            "",
            0
        )
        val Example = Promo(
            "DFSALE",
            0
        )
    }
}

@JvmInline
value class Bonus(
    val amount: Int
)

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

data class OrderItem(
    val id: String,
    val name: String,
    val basePriceFor1: Int,
    val count: Int,
    val photoId: Int
) {
    val basePrice: Int
        get() = basePriceFor1 * count

    fun getDiscountedPrice(promo: Promo): Int {
        val discount = basePrice * promo.amountPercents / 100
        return basePrice - discount
    }
}
