package com.example.dentalfirst.navigation

import kotlinx.serialization.Serializable


@Serializable
object OrderRoute

@Serializable
object AddressPickerRoute


@Serializable
data class MapRoute(
    val initialLat: Double,
    val initialLng: Double
)