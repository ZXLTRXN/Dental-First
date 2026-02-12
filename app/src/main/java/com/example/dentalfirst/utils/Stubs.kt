package com.example.dentalfirst.utils

import com.example.dentalfirst.Customer
import com.example.dentalfirst.OrderState
import com.example.dentalfirst.R


val customerStub = Customer(
    "Иван Зубоделов",
    "+7 (900) 500-40-30",

    R.drawable.ivan_photo
)

val orderStateStub = OrderState(
    "12.02.2026", 4069, customerStub,
)