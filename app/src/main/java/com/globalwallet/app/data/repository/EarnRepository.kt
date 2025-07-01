package com.globalwallet.app.data.repository

import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EarnRepository @Inject constructor() {
    private val tasks = listOf(
        EarnActivity("1", "user1", EarnActivityType.VIDEO_WATCH, BigDecimal("3.0"), "Watch a 10-second video", LocalDateTime.now(), false),
        EarnActivity("2", "user1", EarnActivityType.P2P_TRADE, BigDecimal("4.0"), "Complete a P2P trade", LocalDateTime.now(), false),
        EarnActivity("3", "user1", EarnActivityType.SOCIAL_SHARE, BigDecimal("1.5"), "Share on social media", LocalDateTime.now(), false),
        EarnActivity("4", "user1", EarnActivityType.DAILY_LOGIN, BigDecimal("15.0"), "Login for 7 consecutive days", LocalDateTime.now(), false),
        EarnActivity("5", "user1", EarnActivityType.PROFILE_SETUP, BigDecimal("5.0"), "Complete your profile", LocalDateTime.now(), false)
    )
    private val completedTasks = mutableListOf<EarnActivity>()
    private var totalEarned: BigDecimal = BigDecimal("45.75")
    private var totalReferrals: Int = 7

    fun initialize() {}

    fun getTasks() = tasks
    fun getCompletedTasks() = completedTasks
    fun getTotalEarned() = totalEarned
    fun getTotalReferrals() = totalReferrals

    fun completeTask(taskId: String): BigDecimal {
        val task = tasks.find { it.id == taskId } ?: return BigDecimal.ZERO
        completedTasks.add(task.copy(isCompleted = true))
        totalEarned += task.reward
        return task.reward
    }
} 