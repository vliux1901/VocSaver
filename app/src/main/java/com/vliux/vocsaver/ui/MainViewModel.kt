package com.vliux.vocsaver.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vliux.vocsaver.data.WordRepository
import kotlinx.coroutines.flow.map

class MainViewModel(repository: WordRepository) : ViewModel() {
    val words = repository.allWords.map { list -> list.sortedByDescending { it.added_ts } }
}

class MainViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}