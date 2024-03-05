package com.mahmoudibrahem.taskii.ui.screens.create_task

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.taskii.model.Task
import com.mahmoudibrahem.taskii.repository.database.DatabaseRepository
import com.mahmoudibrahem.taskii.util.Formatter.formatDate
import com.mahmoudibrahem.taskii.util.Formatter.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTaskScreenUIState())
    val uiState = _uiState.asStateFlow()

    private var taskId: Int? = null

    private fun checkFormValidation() {
        if (
            uiState.value.taskName.isNotEmpty() &&
            uiState.value.taskDescription.isNotEmpty() &&
            uiState.value.deadlineDate.isNotEmpty() &&
            uiState.value.deadlineTime.isNotEmpty() &&
            uiState.value.checkList.isNotEmpty()
        ) {
            _uiState.update { it.copy(createButtonEnabled = true) }
        } else {
            _uiState.update { it.copy(createButtonEnabled = false) }
        }
    }

    private fun isEnableResetButton() {
        if (
            uiState.value.taskName.isNotEmpty() ||
            uiState.value.taskDescription.isNotEmpty() ||
            uiState.value.deadlineDate.isNotEmpty() ||
            uiState.value.deadlineTime.isNotEmpty() ||
            uiState.value.checkList.isNotEmpty()
        ) {
            _uiState.update { it.copy(resetButtonEnabled = true) }
        } else {
            _uiState.update { it.copy(resetButtonEnabled = false) }
        }
    }

    fun getTaskById(id: Int) {
        taskId = id
        viewModelScope.launch(Dispatchers.IO) {
            val task = databaseRepository.getTaskById(id)
            _uiState.update {
                it.copy(
                    taskName = task.name,
                    taskDescription = task.description,
                    deadlineDate = formatDate(task.deadline),
                    deadlineTime = formatTime(task.deadline),
                )
            }
        }
    }

    fun getCheckListByTaskId(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    checkList = databaseRepository
                        .getCheckItemsOfTask(taskId)
                        .map { item -> item.content }
                        .toMutableStateList()
                )
            }
        }
    }


    fun onResetClicked() {
        _uiState.update {
            it.copy(
                taskName = "",
                taskDescription = "",
                deadlineDate = "",
                deadlineTime = "",
                checkList = emptyList<String>().toMutableStateList()
            )
        }
    }

    fun onCreateBtnClicked() {
        if (taskId != null) {
            Log.d("```TAG```", "onCreateBtnClicked: $taskId ")
            val task = Task(
                id = taskId!!,
                name = uiState.value.taskName,
                description = uiState.value.taskDescription,
                deadline = LocalDateTime.of(uiState.value.selectedDate, uiState.value.selectedTime),
            )
            updateTask(task = task, checkList = uiState.value.checkList)
        } else {
            val task = Task(
                name = uiState.value.taskName,
                description = uiState.value.taskDescription,
                deadline = LocalDateTime.of(uiState.value.selectedDate, uiState.value.selectedTime),
            )
            createNewTask(task = task, checkList = uiState.value.checkList)
        }
    }

    fun onExpandClicked() {
        _uiState.update { it.copy(checkListExpanded = !uiState.value.checkListExpanded) }
    }

    fun onDone(value: String) {
        _uiState.update {
            it.copy(
                checkList = uiState.value.checkList.apply { add(value) },
                checkItem = ""
            )
        }
        checkFormValidation()
        isEnableResetButton()
    }

    fun onTaskNameChanged(newValue: String) {
        _uiState.update { it.copy(taskName = newValue) }
        checkFormValidation()
        isEnableResetButton()
    }

    fun onTaskDescriptionChanged(newValue: String) {
        _uiState.update { it.copy(taskDescription = newValue) }
        checkFormValidation()
        isEnableResetButton()
    }

    fun onCheckItemTextChanged(newValue: String) {
        _uiState.update { it.copy(checkItem = newValue) }
    }

    fun onDeadlineDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date, deadlineDate = date.toString()) }
        checkFormValidation()
        isEnableResetButton()
    }

    fun onDeadlineTimeSelected(time: LocalTime) {
        _uiState.update { it.copy(selectedTime = time, deadlineTime = time.toString()) }
        checkFormValidation()
        isEnableResetButton()
    }


    private fun createNewTask(task: Task, checkList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.createNewTask(task = task, checkList = checkList)
        }
    }

    private fun updateTask(task: Task, checkList: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.updateTask(task, checkList)
        }
    }

}