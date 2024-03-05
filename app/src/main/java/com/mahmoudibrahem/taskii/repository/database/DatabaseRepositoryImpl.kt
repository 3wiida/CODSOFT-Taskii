package com.mahmoudibrahem.taskii.repository.database

import androidx.room.withTransaction
import com.mahmoudibrahem.taskii.data.local.dao.CheckItemDao
import com.mahmoudibrahem.taskii.data.local.dao.TasksDao
import com.mahmoudibrahem.taskii.data.local.database.TaskiiDatabase
import com.mahmoudibrahem.taskii.model.CheckItem
import com.mahmoudibrahem.taskii.model.Task
import javax.inject.Inject

class DatabaseRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val checkItemDao: CheckItemDao,
    private val db: TaskiiDatabase
) : DatabaseRepository {

    override suspend fun upsertTask(task: Task) {
        tasksDao.upsertTask(task)
    }

    override suspend fun getAllTasks(): List<Task> {
        return tasksDao.getAllTasks()
    }

    override suspend fun upsertCheckItem(checkItem: CheckItem) {
        checkItemDao.upsertCheckItem(checkItem)
    }

    override suspend fun getCheckItemsOfTask(taskId: Int): List<CheckItem> {
        return checkItemDao.getCheckItemsOfTask(taskId)
    }

    override suspend fun searchTask(searchQuery: String): List<Task> {
        return tasksDao.searchForTask(searchQuery)
    }

    override suspend fun getLatestTaskId(): Int {
        return tasksDao.getLatestTaskId()
    }


    override suspend fun createNewTask(task: Task, checkList: List<String>) {
        db.withTransaction {
            upsertTask(task)
            val latestTaskId = getLatestTaskId()
            checkList.forEach { content ->
                val checkItem = CheckItem(taskId = latestTaskId, content = content)
                upsertCheckItem(checkItem)
            }
        }
    }

    override suspend fun saveTaskProcess(task: Task, checkItem: CheckItem) {
        db.withTransaction {
            upsertCheckItem(checkItem)
            upsertTask(task)
        }
    }

    override suspend fun getTaskById(id: Int): Task {
        return tasksDao.getTaskById(id)
    }

    override suspend fun deleteTask(id: Int) {
        tasksDao.deleteTask(id)
    }

    override suspend fun getCompletedTasks(): List<Task> {
        return tasksDao.getCompletedTasks()
    }

    override suspend fun getUnCompletedTasks(): List<Task> {
        return tasksDao.getUnCompletedTasks()
    }

    override suspend fun deleteOldCheckList(taskId: Int) {
        checkItemDao.deleteOldCheckList(taskId)
    }

    override suspend fun updateTask(task: Task, checkList: List<String>) {
        db.withTransaction {
            upsertTask(task)
            val oldChecklist = getCheckItemsOfTask(task.id)
            val newItems = mutableListOf<String>()
            oldChecklist.forEach{oldItem->
                if(!checkList.contains(oldItem.content)){
                    newItems.add(oldItem.content)
                }
            }
            newItems.forEach { content ->
                val checkItem = CheckItem(taskId = task.id, content = content)
                upsertCheckItem(checkItem)
            }
        }
    }
}