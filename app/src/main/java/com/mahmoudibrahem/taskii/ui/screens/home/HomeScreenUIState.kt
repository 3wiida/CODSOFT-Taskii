package com.mahmoudibrahem.taskii.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.model.Task

data class HomeScreenUIState(
    val username: String = "",
    val tasks: SnapshotStateList<Task> = mutableStateListOf(),
    val currentCheckList: SnapshotStateList<CheckItem> = mutableStateListOf(),
    val selectedTaskIndex: Int = 0,
    val isShowEmptyState: Boolean = false
)