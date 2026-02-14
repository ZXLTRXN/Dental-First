package com.example.dentalfirst.utils

fun Int.toPriceString(): String {
    val remainder = this % 100f
    return if (remainder == 0f) "${this / 100}"
    else "${this / 100}.${this % 100}"
}
