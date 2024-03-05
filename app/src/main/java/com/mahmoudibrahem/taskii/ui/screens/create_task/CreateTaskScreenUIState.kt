package com.mahmoudibrahem.taskii.ui.screens.create_task

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mahmoudibrahem.taskii.model.CheckItem
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class CreateTaskScreenUIState(
    val taskName: String = "",
    val taskDescription: String = "",
    val deadlineDate: String = "",
    val deadlineTime: String = "",
    val checkItem: String = "",
    val selectedDate: LocalDate = LocalDate.now(),
    val selectedTime: LocalTime = LocalTime.now(),
    val checkList: SnapshotStateList<String> = mutableStateListOf(),
    val createButtonEnabled: Boolean = false,
    val resetButtonEnabled: Boolean = false,
    val checkListExpanded: Boolean = false
)
