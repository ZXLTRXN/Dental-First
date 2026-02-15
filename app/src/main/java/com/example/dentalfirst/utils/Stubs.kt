package com.example.dentalfirst.utils

import com.example.dentalfirst.Bonus
import com.example.dentalfirst.Customer
import com.example.dentalfirst.OrderItem
import com.example.dentalfirst.OrderState
import com.example.dentalfirst.Promo
import com.example.dentalfirst.R


val customerStub = Customer(
    "Иван Зубоделов",
    "+7 (900) 500-40-30",
    R.drawable.ivan_photo
)

val orderItemsStub = listOf(
    OrderItem(
        id = "1",
        name = "Жидкость для антисептической обработки корневых каналов, 13 мл",
        basePriceFor1 = 568700,
        count = 1,
        photoId = R.drawable.item_1
    ),
    OrderItem(
        id = "2",
        name = "4-слойные салфетки Mesoft, стерильные 40 г. 7,5х7,5 см 150 шт",
        basePriceFor1 = 37000,
        count = 3,
        photoId = R.drawable.item_2
    )
)

val orderStateStub = OrderState(
    date = "12.02.2026",
    id = 4069,
    customer = customerStub,
    items = orderItemsStub,
    appliedPromo = Promo.None,
    userBonuses = Bonus(120000),
    courierDates = listOf(
        "Вт, 17.02 (завтра)",
        "Ср, 18.02",
        "Чт, 19.02",
        "Пт, 20.02",
        "Сб, 21.02"
    )
)