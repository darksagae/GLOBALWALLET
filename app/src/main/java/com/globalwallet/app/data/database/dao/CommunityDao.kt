package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.Community
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {
    
    @Query("SELECT * FROM communities ORDER BY memberCount DESC")
    fun getAllCommunities(): Flow<List<Community>>
    
    @Query("SELECT * FROM communities WHERE id = :communityId")
    suspend fun getCommunityById(communityId: String): Community?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunity(community: Community)
    
    @Update
    suspend fun updateCommunity(community: Community)
    
    @Delete
    suspend fun deleteCommunity(community: Community)
    
    @Query("UPDATE communities SET memberCount = memberCount + 1 WHERE id = :communityId")
    suspend fun incrementMemberCount(communityId: String)
    
    @Query("UPDATE communities SET memberCount = memberCount - 1 WHERE id = :communityId")
    suspend fun decrementMemberCount(communityId: String)
} 