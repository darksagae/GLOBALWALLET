package com.globalwallet.app.data.repository

import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiscoverRepository @Inject constructor() {
    private val dapps = listOf(
        DApp("1", "PancakeSwap", "DEX for BEP-20", "https://pancakeswap.finance", "bsc", DAppCategory.DEX, BigDecimal("0.3"), true),
        DApp("2", "QuickSwap", "DEX for Polygon", "https://quickswap.exchange", "polygon", DAppCategory.DEX, BigDecimal("0.3"), true),
        DApp("3", "Raydium", "DEX for Solana", "https://raydium.io", "solana", DAppCategory.DEX, BigDecimal("0.3"), true)
    )
    private val trendingTokens = listOf<TokenInfo>()
    private val news = listOf<NewsItem>()

    fun initialize() {}

    fun getDApps() = dapps
    fun getTrendingTokens() = trendingTokens
    fun getNews() = news
    fun visitDApp(dappId: String): BigDecimal {
        return BigDecimal("0.3")
    }
} 