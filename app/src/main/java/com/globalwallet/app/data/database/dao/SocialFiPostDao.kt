package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.SocialFiPost
import kotlinx.coroutines.flow.Flow

@Dao
interface SocialFiPostDao {
    
    @Query("SELECT * FROM socialfi_posts ORDER BY createdAt DESC")
    fun getAllPosts(): Flow<List<SocialFiPost>>
    
    @Query("SELECT * FROM socialfi_posts WHERE userId = :userId ORDER BY createdAt DESC")
    fun getPostsByUserId(userId: String): Flow<List<SocialFiPost>>
    
    @Query("SELECT * FROM socialfi_posts WHERE isVerified = 1 ORDER BY createdAt DESC")
    fun getVerifiedPosts(): Flow<List<SocialFiPost>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: SocialFiPost)
    
    @Update
    suspend fun updatePost(post: SocialFiPost)
    
    @Delete
    suspend fun deletePost(post: SocialFiPost)
    
    @Query("UPDATE socialfi_posts SET likes = likes + 1 WHERE id = :postId")
    suspend fun incrementLikes(postId: String)
    
    @Query("UPDATE socialfi_posts SET comments = comments + 1 WHERE id = :postId")
    suspend fun incrementComments(postId: String)
    
    @Query("UPDATE socialfi_posts SET shares = shares + 1 WHERE id = :postId")
    suspend fun incrementShares(postId: String)
    
    @Query("DELETE FROM socialfi_posts WHERE userId = :userId")
    suspend fun deletePostsByUserId(userId: String)
} 