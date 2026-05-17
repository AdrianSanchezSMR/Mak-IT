package com.makit.tfg.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.makit.tfg.data.MakItRepository

class MakItViewModelFactory(
    private val repository: MakItRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MakItAppState::class.java)) {
            return MakItAppState(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
