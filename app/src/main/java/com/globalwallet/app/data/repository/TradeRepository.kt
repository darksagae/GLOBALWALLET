package com.globalwallet.app.data.repository

import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TradeRepository @Inject constructor() {
    private val p2pListings = listOf(
        P2PListing("1", "seller1", "ETH", BigDecimal("0.5"), BigDecimal("1800.00"), "Bank Transfer", P2PStatus.ACTIVE, LocalDateTime.now()),
        P2PListing("2", "seller2", "BNB", BigDecimal("2.0"), BigDecimal("500.00"), "PayPal", P2PStatus.ACTIVE, LocalDateTime.now())
    )
    private val recentTrades = listOf<Transaction>()

    fun initialize() {}

    fun getP2PListings() = p2pListings
    fun getRecentTrades() = recentTrades
    fun completeTrade(listingId: String): BigDecimal {
        return BigDecimal("0.75")
    }
} 