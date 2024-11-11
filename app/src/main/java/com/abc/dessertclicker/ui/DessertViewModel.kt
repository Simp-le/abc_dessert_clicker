package com.abc.dessertclicker.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.abc.dessertclicker.R
import com.abc.dessertclicker.data.Datasource.dessertList
import com.abc.dessertclicker.data.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel : ViewModel() {
    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val dessertSold = currentState.dessertsSold.inc()
            val nextDessertIndex = determineDessertIndex(dessertSold)
            currentState.copy(
                revenue = currentState.revenue + currentState.currentDessertPrice,
                dessertsSold = dessertSold,
                currentDessertImageId = dessertList[nextDessertIndex].imageId,
                currentDessertPrice = dessertList[nextDessertIndex].price,
                currentDessertIndex = nextDessertIndex
            )
        }
    }

    /**
     * Determine which dessert to show.
     */
    private fun determineDessertIndex(dessertsSold: Int): Int {
        var dessertIndex = 0
        for (index in dessertList.indices) {
            if (dessertsSold >= dessertList[index].startProductionAmount) {
                dessertIndex = index
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }
        return dessertIndex
    }
}