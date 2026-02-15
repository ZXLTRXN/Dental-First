package com.example.dentalfirst

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf

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
    val isSelected: MutableState<Boolean> = mutableStateOf(false), // можно и так,
    // но более размазано получается, лучше все через copy
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

fun List<DeliveryItem>.select(item: DeliveryItem) {
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