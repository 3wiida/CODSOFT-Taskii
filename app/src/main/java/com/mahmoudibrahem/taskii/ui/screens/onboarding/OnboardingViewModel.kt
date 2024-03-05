package com.mahmoudibrahem.taskii.ui.screens.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUIState())
    val uiState=_uiState.asStateFlow()
}