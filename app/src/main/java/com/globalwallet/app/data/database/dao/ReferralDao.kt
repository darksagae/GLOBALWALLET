package com.globalwallet.app.data.database.dao

import androidx.room.*
import com.globalwallet.app.data.model.Referral
import kotlinx.coroutines.flow.Flow

@Dao
interface ReferralDao {
    
    @Query("SELECT * FROM referrals WHERE referrerId = :referrerId")
    fun getReferralsByReferrerId(referrerId: String): Flow<List<Referral>>
    
    @Query("SELECT * FROM referrals WHERE referredId = :referredId")
    suspend fun getReferralByReferredId(referredId: String): Referral?
    
    @Query("SELECT COUNT(*) FROM referrals WHERE referrerId = :referrerId AND status = 'COMPLETED'")
    suspend fun getCompletedReferralsCount(referrerId: String): Int
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReferral(referral: Referral)
    
    @Update
    suspend fun updateReferral(referral: Referral)
    
    @Delete
    suspend fun deleteReferral(referral: Referral)
    
    @Query("UPDATE referrals SET status = 'COMPLETED' WHERE id = :referralId")
    suspend fun markReferralAsCompleted(referralId: String)
    
    @Query("DELETE FROM referrals WHERE referrerId = :referrerId")
    suspend fun deleteReferralsByReferrerId(referrerId: String)
} 