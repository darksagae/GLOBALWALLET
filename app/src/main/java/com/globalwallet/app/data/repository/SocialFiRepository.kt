package com.globalwallet.app.data.repository

import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialFiRepository @Inject constructor() {
    private val posts = mutableListOf(
        SocialFiPost("1", "user1", "Hello Web3!", 5, 2, 1, BigDecimal("2.0"), LocalDateTime.now(), true),
        SocialFiPost("2", "user1", "Just joined Global Wallet!", 3, 1, 0, BigDecimal("2.0"), LocalDateTime.now(), true)
    )
    private val communities = listOf(
        Community("1", "Global Wallet Enthusiasts", "Official community", 100, BigDecimal("3.0"), LocalDateTime.now()),
        Community("2", "Polygon Fans", "Polygon chain lovers", 50, BigDecimal("3.0"), LocalDateTime.now())
    )
    private var totalEarned: BigDecimal = BigDecimal("10.0")

    fun initialize() {}

    fun getPosts() = posts
    fun getCommunities() = communities
    fun getTotalEarned() = totalEarned

    fun createPost(content: String): BigDecimal {
        val reward = BigDecimal("2.0")
        posts.add(SocialFiPost(UUID.randomUUID().toString(), "user1", content, 0, 0, 0, reward, LocalDateTime.now(), true))
        totalEarned += reward
        return reward
    }

    fun joinCommunity(communityId: String): BigDecimal {
        val reward = BigDecimal("3.0")
        totalEarned += reward
        return reward
    }
} 