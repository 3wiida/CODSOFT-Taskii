package com.mahmoudibrahem.taskii.ui.screens.search

import com.mahmoudibrahem.taskii.model.Task

data class SearchScreenUIState(
    val query: String = "",
    val showEmptyState: Boolean = false,
    val results: List<Task> = emptyList(),
)
