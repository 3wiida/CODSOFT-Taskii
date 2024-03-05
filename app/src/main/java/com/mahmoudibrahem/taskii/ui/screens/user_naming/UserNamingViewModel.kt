package com.mahmoudibrahem.taskii.ui.screens.user_naming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.taskii.repository.data_store.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserNamingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserNamingUIState())
    val uiState = _uiState.asStateFlow()

    private fun saveOnboardingState() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveOnboardingState(state = false)
        }
    }

    private fun saveUsername(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveUserName(name = name)
        }
    }

    fun onNameValueChanged(name: String) {
        _uiState.update { it.copy(username = name) }
    }

    fun onConfirmBtnClicked(){
        saveUsername(name = uiState.value.username)
        saveOnboardingState()
    }
}