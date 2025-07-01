package com.globalwallet.app.data.repository

import com.globalwallet.app.data.model.MiningSession
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MiningRepository @Inject constructor() {
    private var lastMiningTime: LocalDateTime = LocalDateTime.now().minusHours(8)
    private var currentStreak: Int = 3
    private var totalMined: BigDecimal = BigDecimal("12.5")

    fun initialize() {
        // Load mining data from secure storage or mock
    }

    fun getLastMiningTime() = lastMiningTime
    fun getCurrentStreak() = currentStreak
    fun getTotalMined() = totalMined

    fun mine(): BigDecimal {
        val baseReward = BigDecimal("1.5")
        val streakBonus = if (currentStreak > 0) BigDecimal("0.5") else BigDecimal.ZERO
        val reward = baseReward + streakBonus
        totalMined += reward
        currentStreak++
        lastMiningTime = LocalDateTime.now()
        return reward
    }
} 