package com.example.dentalfirst

import android.app.Application
import com.yandex.mapkit.MapKitFactory

class DentalApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey("")
    }
}