package com.globalwallet.app.data.repository

import com.globalwallet.app.data.model.User
import com.globalwallet.app.data.security.SecurityManager
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val securityManager: SecurityManager
) {
    private var currentUser: User? = null

    fun initialize() {
        // Load user from secure storage or mock
        currentUser = User(
            id = "user1",
            username = "DemoUser",
            email = "demo@globalwallet.com",
            did = "did:mock:1234567890",
            referralCode = "GLWDEMO123",
            referredBy = null,
            totalEarned = java.math.BigDecimal("45.75"),
            totalMined = java.math.BigDecimal("12.5"),
            totalReferrals = 7,
            createdAt = LocalDateTime.now().minusMonths(1),
            lastLogin = LocalDateTime.now(),
            isBiometricEnabled = true,
            preferredCurrency = "USD"
        )
    }

    fun getCurrentUser(): User? = currentUser

    fun updatePreferredCurrency(currency: String) {
        currentUser = currentUser?.copy(preferredCurrency = currency)
    }

    fun updateBiometricEnabled(enabled: Boolean) {
        currentUser = currentUser?.copy(isBiometricEnabled = enabled)
    }

    fun updateDID(did: String) {
        currentUser = currentUser?.copy(did = did)
    }
} 