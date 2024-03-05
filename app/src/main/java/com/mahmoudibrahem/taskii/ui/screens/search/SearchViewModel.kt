package com.mahmoudibrahem.taskii.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.taskii.repository.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun searchForTasks(searchQuery: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if(uiState.value.query.isNotEmpty()){
                _uiState.update { it.copy(results = databaseRepository.searchTask(searchQuery)) }
                delay(1000)
                _uiState.update { it.copy(showEmptyState = true) }
            }
        }
    }

    fun onQueryChanged(newQuery: String) {
        _uiState.update { it.copy(query = newQuery) }
    }

    fun onSearchClicked() {
        searchForTasks(searchQuery = uiState.value.query)
    }

    fun onClearClicked() {
        _uiState.update { it.copy(query = "", results = emptyList(), showEmptyState = false) }
    }


}