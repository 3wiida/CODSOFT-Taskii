package com.mahmoudibrahem.taskii.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.mahmoudibrahem.taskii.model.CheckItem

@Dao
interface CheckItemDao {
    @Upsert
    suspend fun upsertCheckItem(checkItem: CheckItem)

    @Query("SELECT * FROM CheckItem WHERE taskId=:taskId")
    suspend fun getCheckItemsOfTask(taskId: Int): List<CheckItem>

    @Query("DELETE FROM CheckItem WHERE taskId=:taskId")
    suspend fun deleteOldCheckList(taskId: Int)
}