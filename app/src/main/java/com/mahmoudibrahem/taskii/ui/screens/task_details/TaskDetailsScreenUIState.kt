package com.mahmoudibrahem.taskii.ui.screens.task_details

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.model.Task

data class TaskDetailsScreenUIState(
    val task: Task = Task(),
    val checkList: SnapshotStateList<CheckItem> = mutableStateListOf()
)
