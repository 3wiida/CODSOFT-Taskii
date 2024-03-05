package com.mahmoudibrahem.taskii.ui.screens.home

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.repository.data_store.DataStoreRepository
import com.mahmoudibrahem.taskii.repository.database.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUIState())
    val uiState = _uiState.asStateFlow()

    init {
        observeUsername()
    }

    fun onTaskClicked(index: Int, id: Int) {
        getCheckListByTaskId(id)
        _uiState.update { it.copy(selectedTaskIndex = index) }
    }

    fun onCompleteCheckItem(checkItem: CheckItem, isCheckItemCompleted: Boolean, index: Int) {
        saveTaskProcess(
            checkItem = checkItem,
            taskIndex = uiState.value.selectedTaskIndex,
            checkItemIndex = index,
            isCheckItemCompleted = isCheckItemCompleted
        )
    }

    fun onTaskCompleted() {
        getUncompletedTasks()
        _uiState.update { it.copy(selectedTaskIndex = 0) }
    }

    private fun observeUsername() {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.readUsername().collectLatest { name ->
                name?.let { value -> _uiState.update { it.copy(username = value) } }
            }
        }
    }

    fun getUncompletedTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    tasks = databaseRepository.getUnCompletedTasks().toMutableStateList()
                )
            }
            if (uiState.value.tasks.isNotEmpty()) {
                getCheckListByTaskId(taskId = uiState.value.tasks[0].id)
            }
            _uiState.update { it.copy(isShowEmptyState = true) }
        }
    }

    private fun getCheckListByTaskId(taskId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update {
                it.copy(
                    currentCheckList = databaseRepository.getCheckItemsOfTask(
                        taskId
                    ).toMutableStateList()
                )
            }
        }
    }

    private fun saveTaskProcess(
        checkItem: CheckItem,
        taskIndex: Int,
        checkItemIndex: Int,
        isCheckItemCompleted: Boolean
    ) {
        uiState.value.currentCheckList[checkItemIndex] =
            checkItem.copy(isComplete = isCheckItemCompleted)
        uiState.value.tasks[taskIndex] =
            uiState.value.tasks[taskIndex].copy(progress = getNewProgress())
        viewModelScope.launch(Dispatchers.IO) {
            databaseRepository.saveTaskProcess(
                task = uiState.value.tasks[taskIndex],
                checkItem = uiState.value.currentCheckList[checkItemIndex]
            )
        }
    }

    private fun getNewProgress(): Float {
        val totalItems = uiState.value.currentCheckList.size
        val completedItems = uiState.value.currentCheckList.filter { it.isComplete }.size
        return completedItems.div(totalItems.toFloat())
    }


}