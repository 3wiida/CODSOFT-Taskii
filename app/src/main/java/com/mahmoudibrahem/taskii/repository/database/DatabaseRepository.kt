package com.mahmoudibrahem.taskii.repository.database

import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.model.Task

interface DatabaseRepository {
    suspend fun upsertTask(task: Task)
    suspend fun getAllTasks(): List<Task>
    suspend fun upsertCheckItem(checkItem: CheckItem)
    suspend fun getCheckItemsOfTask(taskId: Int): List<CheckItem>
    suspend fun searchTask(searchQuery: String): List<Task>
    suspend fun getLatestTaskId(): Int
    suspend fun createNewTask(task: Task, checkList: List<String>)
    suspend fun saveTaskProcess(task: Task, checkItem: CheckItem)
    suspend fun getTaskById(id: Int): Task
    suspend fun deleteTask(id: Int)
    suspend fun getCompletedTasks(): List<Task>
    suspend fun getUnCompletedTasks(): List<Task>
    suspend fun deleteOldCheckList(taskId: Int)
    suspend fun updateTask(task: Task, checkList: List<String>)
}