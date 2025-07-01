package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.EarnActivity
import kotlinx.coroutines.flow.Flow

@Dao
interface EarnActivityDao {
    
    @Query("SELECT * FROM earn_activities WHERE userId = :userId AND isCompleted = 0")
    fun getAvailableTasks(userId: String): Flow<List<EarnActivity>>
    
    @Query("SELECT * FROM earn_activities WHERE userId = :userId AND isCompleted = 1")
    fun getCompletedTasks(userId: String): Flow<List<EarnActivity>>
    
    @Query("SELECT * FROM earn_activities WHERE userId = :userId AND type = :type")
    fun getTasksByType(userId: String, type: String): Flow<List<EarnActivity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEarnActivity(earnActivity: EarnActivity)
    
    @Update
    suspend fun updateEarnActivity(earnActivity: EarnActivity)
    
    @Delete
    suspend fun deleteEarnActivity(earnActivity: EarnActivity)
    
    @Query("UPDATE earn_activities SET isCompleted = 1 WHERE id = :taskId")
    suspend fun markTaskAsCompleted(taskId: String)
    
    @Query("DELETE FROM earn_activities WHERE userId = :userId")
    suspend fun deleteEarnActivitiesByUserId(userId: String)
} 