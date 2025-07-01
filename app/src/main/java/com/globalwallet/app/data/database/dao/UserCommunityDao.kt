package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.UserCommunity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCommunityDao {
    
    @Query("SELECT * FROM user_communities WHERE userId = :userId")
    fun getUserCommunities(userId: String): Flow<List<UserCommunity>>
    
    @Query("SELECT * FROM user_communities WHERE communityId = :communityId")
    fun getCommunityMembers(communityId: String): Flow<List<UserCommunity>>
    
    @Query("SELECT * FROM user_communities WHERE userId = :userId AND communityId = :communityId")
    suspend fun getUserCommunity(userId: String, communityId: String): UserCommunity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserCommunity(userCommunity: UserCommunity)
    
    @Update
    suspend fun updateUserCommunity(userCommunity: UserCommunity)
    
    @Delete
    suspend fun deleteUserCommunity(userCommunity: UserCommunity)
    
    @Query("DELETE FROM user_communities WHERE userId = :userId")
    suspend fun deleteUserCommunitiesByUserId(userId: String)
    
    @Query("DELETE FROM user_communities WHERE communityId = :communityId")
    suspend fun deleteUserCommunitiesByCommunityId(communityId: String)
} 