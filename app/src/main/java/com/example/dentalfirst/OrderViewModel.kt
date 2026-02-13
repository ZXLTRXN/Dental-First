package com.example.dentalfirst

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dentalfirst.utils.orderStateStub
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class OrderViewModel: ViewModel() {

    private var _orderState by mutableStateOf(orderStateStub)
    val orderState: OrderState get() = _orderState

    private val intent: Channel<OrderIntent> = Channel(Channel.BUFFERED)// fixme

    init {
        handleIntents()
    }

    fun processIntent(intent: OrderIntent) {
        viewModelScope.launch {
            this@OrderViewModel.intent.send(intent)
        }
    }

    private fun handleIntents() {
        viewModelScope.launch {
            intent.consumeAsFlow().collect { intent ->
                when (intent) {
                    is OrderIntent.SelectFulfillmentType-> selectFulfillmentType(intent.type)
//                    is OrderIntent.SelectPickup -> selectFulfillmentType(FulfillmentType.PICKUP)
                }
            }
        }
    }

    private fun selectFulfillmentType(type: FulfillmentType) {
        _orderState._selectedFulfillmentType = type
    }


}

sealed interface OrderIntent {
    data class SelectFulfillmentType(val type: FulfillmentType): OrderIntent
//    object SelectPickup: OrderIntent
}