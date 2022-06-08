package com.example.rickandmortyapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.rickandmortyapi.Repositories.Repository

class SharedViewModelFactory(
    private val repository: Repository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(repository) as T
    }
}