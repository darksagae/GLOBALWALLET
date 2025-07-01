package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.UserBadge
import kotlinx.coroutines.flow.Flow

@Dao
interface UserBadgeDao {
    
    @Query("SELECT * FROM user_badges WHERE userId = :userId ORDER BY earnedAt DESC")
    fun getUserBadges(userId: String): Flow<List<UserBadge>>
    
    @Query("SELECT * FROM user_badges WHERE userId = :userId AND badgeType = :badgeType")
    suspend fun getUserBadgeByType(userId: String, badgeType: String): UserBadge?
    
    @Query("SELECT COUNT(*) FROM user_badges WHERE userId = :userId")
    suspend fun getUserBadgeCount(userId: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserBadge(userBadge: UserBadge)
    
    @Update
    suspend fun updateUserBadge(userBadge: UserBadge)
    
    @Delete
    suspend fun deleteUserBadge(userBadge: UserBadge)
    
    @Query("DELETE FROM user_badges WHERE userId = :userId")
    suspend fun deleteUserBadgesByUserId(userId: String)
} 