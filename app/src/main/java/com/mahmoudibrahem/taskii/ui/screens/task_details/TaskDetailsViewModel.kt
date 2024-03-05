package com.mahmoudibrahem.taskii.ui.screens.task_details

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.repository.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskDetailsScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun onCompleteTaskItem(checkItem: CheckItem, isCompleted: Boolean, index: Int) {
        saveTaskProcess(checkItem, index, isCompleted)
    }

    fun onDeleteTaskClicked() {
        deleteTask()
    }

    fun getTaskById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(task = databaseRepository.getTaskById(id)) }
        }
    }

    fun getCheckListByTaskId(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    checkList = databaseRepository.getCheckItemsOfTask(taskId).toMutableStateList()
                )
            }
        }
    }

    private fun saveTaskProcess(
        checkItem: CheckItem,
        checkItemIndex: Int,
        isCheckItemCompleted: Boolean
    ) {
        uiState.value.checkList[checkItemIndex] = checkItem.copy(isComplete = isCheckItemCompleted)
        _uiState.update { it.copy(task = uiState.value.task.copy(progress = getNewProgress())) }
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.saveTaskProcess(
                task = uiState.value.task,
                checkItem = uiState.value.checkList[checkItemIndex]
            )
        }
    }

    private fun getNewProgress(): Float {
        val totalItems = uiState.value.checkList.size
        val completedItems = uiState.value.checkList.filter { it.isComplete }.size
        return completedItems.div(totalItems.toFloat())
    }

    private fun deleteTask() {
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.deleteTask(uiState.value.task.id)
        }
    }
}