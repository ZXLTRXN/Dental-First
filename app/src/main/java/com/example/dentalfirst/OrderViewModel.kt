package com.example.dentalfirst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class OrderViewModel: ViewModel() {

    private val _orderState = mutableStateOf(orderStateStub)
    val orderState: OrderState by _orderState // fixme

    val intent: Channel<OrderIntent> = Channel(Channel.BUFFERED)// fixme

    fun processIntent(intent: OrderIntent) {
        viewModelScope.launch {
            this@OrderViewModel.intent.send(intent)
        }
    }


}

sealed interface OrderIntent {
    object SelectDelivery: OrderIntent
    object SelectPickup: OrderIntent
}