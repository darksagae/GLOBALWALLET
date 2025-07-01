package com.globalwallet.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

/**
 * Data Models for Global Wallet App
 * 
 * Includes models for:
 * - Wallet information and balances
 * - Cryptocurrency transactions
 * - User data and preferences
 * - Mining and earning activities
 * - SocialFi interactions
 */

@Entity(tableName = "wallets")
data class Wallet(
    @PrimaryKey val id: String,
    val mnemonic: String, // Encrypted
    val privateKey: String, // Encrypted
    val publicKey: String,
    val address: String,
    val chain: String,
    val createdAt: LocalDateTime,
    val isActive: Boolean = true
)

@Entity(tableName = "balances")
data class Balance(
    @PrimaryKey val id: String,
    val walletId: String,
    val tokenSymbol: String,
    val tokenName: String,
    val amount: BigDecimal,
    val usdValue: BigDecimal,
    val chain: String,
    val lastUpdated: LocalDateTime
)

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey val id: String,
    val walletId: String,
    val txHash: String,
    val fromAddress: String,
    val toAddress: String,
    val amount: BigDecimal,
    val tokenSymbol: String,
    val gasFee: BigDecimal?,
    val status: TransactionStatus,
    val type: TransactionType,
    val chain: String,
    val timestamp: LocalDateTime,
    val blockNumber: Long?
)

enum class TransactionStatus {
    PENDING, CONFIRMED, FAILED, CANCELLED
}

enum class TransactionType {
    SEND, RECEIVE, SWAP, BRIDGE, MINING_REWARD, EARN_REWARD, REFERRAL_BONUS
}

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val username: String,
    val email: String?,
    val did: String?, // Decentralized Identity
    val referralCode: String,
    val referredBy: String?,
    val totalEarned: BigDecimal = BigDecimal.ZERO,
    val totalMined: BigDecimal = BigDecimal.ZERO,
    val totalReferrals: Int = 0,
    val createdAt: LocalDateTime,
    val lastLogin: LocalDateTime,
    val isBiometricEnabled: Boolean = false,
    val preferredCurrency: String = "USD"
)

@Entity(tableName = "mining_sessions")
data class MiningSession(
    @PrimaryKey val id: String,
    val userId: String,
    val startTime: LocalDateTime,
    val endTime: LocalDateTime?,
    val reward: BigDecimal,
    val streak: Int,
    val isCompleted: Boolean = false
)

@Entity(tableName = "earn_activities")
data class EarnActivity(
    @PrimaryKey val id: String,
    val userId: String,
    val type: EarnActivityType,
    val reward: BigDecimal,
    val description: String,
    val completedAt: LocalDateTime,
    val isCompleted: Boolean = false
)

enum class EarnActivityType {
    REFERRAL, VIDEO_WATCH, P2P_TRADE, SOCIAL_SHARE, DAILY_LOGIN, PROFILE_SETUP, MYSTERY_BOX, SOCIALFI_POST, COMMUNITY_JOIN
}

@Entity(tableName = "referrals")
data class Referral(
    @PrimaryKey val id: String,
    val referrerId: String,
    val referredId: String,
    val referralCode: String,
    val reward: BigDecimal,
    val status: ReferralStatus,
    val createdAt: LocalDateTime
)

enum class ReferralStatus {
    PENDING, COMPLETED, EXPIRED
}

@Entity(tableName = "socialfi_posts")
data class SocialFiPost(
    @PrimaryKey val id: String,
    val userId: String,
    val content: String,
    val likes: Int = 0,
    val comments: Int = 0,
    val shares: Int = 0,
    val reward: BigDecimal = BigDecimal.ZERO,
    val createdAt: LocalDateTime,
    val isVerified: Boolean = false
)

@Entity(tableName = "communities")
data class Community(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val memberCount: Int = 0,
    val reward: BigDecimal = BigDecimal.ZERO,
    val createdAt: LocalDateTime
)

@Entity(tableName = "user_communities")
data class UserCommunity(
    @PrimaryKey val id: String,
    val userId: String,
    val communityId: String,
    val joinedAt: LocalDateTime,
    val reward: BigDecimal = BigDecimal.ZERO
)

@Entity(tableName = "dapps")
data class DApp(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val url: String,
    val chain: String,
    val category: DAppCategory,
    val reward: BigDecimal = BigDecimal.ZERO,
    val isActive: Boolean = true
)

enum class DAppCategory {
    DEX, NFT, GAMING, DEFI, SOCIAL, UTILITY
}

@Entity(tableName = "user_badges")
data class UserBadge(
    @PrimaryKey val id: String,
    val userId: String,
    val badgeType: BadgeType,
    val earnedAt: LocalDateTime,
    val description: String
)

enum class BadgeType {
    SILVER_REFERRER, GOLD_REFERRER, PLATINUM_REFERRER, SOCIALFI_CONTRIBUTOR, MINING_MASTER, TRADING_PRO
}

// Network-specific models
data class NetworkInfo(
    val chainId: String,
    val name: String,
    val rpcUrl: String,
    val explorerUrl: String,
    val nativeToken: String,
    val isTestnet: Boolean = false
)

data class TokenInfo(
    val symbol: String,
    val name: String,
    val decimals: Int,
    val contractAddress: String?,
    val chain: String,
    val price: BigDecimal?,
    val marketCap: BigDecimal?,
    val volume24h: BigDecimal?
)

// UI State models
data class WalletState(
    val totalBalance: BigDecimal = BigDecimal.ZERO,
    val balances: List<Balance> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class MiningState(
    val currentStreak: Int = 0,
    val lastMiningTime: LocalDateTime? = null,
    val nextMiningTime: LocalDateTime? = null,
    val totalMined: BigDecimal = BigDecimal.ZERO,
    val isMiningAvailable: Boolean = false,
    val isLoading: Boolean = false
)

data class EarnState(
    val totalEarned: BigDecimal = BigDecimal.ZERO,
    val availableTasks: List<EarnActivity> = emptyList(),
    val completedTasks: List<EarnActivity> = emptyList(),
    val referralStats: ReferralStats = ReferralStats(),
    val isLoading: Boolean = false
)

data class ReferralStats(
    val totalReferrals: Int = 0,
    val totalEarned: BigDecimal = BigDecimal.ZERO,
    val currentTier: ReferralTier = ReferralTier.BRONZE,
    val nextTierProgress: Float = 0f
)

enum class ReferralTier {
    BRONZE, SILVER, GOLD, PLATINUM
}

data class SocialFiState(
    val posts: List<SocialFiPost> = emptyList(),
    val communities: List<Community> = emptyList(),
    val userCommunities: List<UserCommunity> = emptyList(),
    val totalEarned: BigDecimal = BigDecimal.ZERO,
    val isLoading: Boolean = false
)

data class TradeState(
    val availableTokens: List<TokenInfo> = emptyList(),
    val recentTrades: List<Transaction> = emptyList(),
    val p2pListings: List<P2PListing> = emptyList(),
    val isLoading: Boolean = false
)

data class P2PListing(
    val id: String,
    val sellerId: String,
    val tokenSymbol: String,
    val amount: BigDecimal,
    val price: BigDecimal,
    val paymentMethod: String,
    val status: P2PStatus,
    val createdAt: LocalDateTime
)

enum class P2PStatus {
    ACTIVE, SOLD, CANCELLED, EXPIRED
}

data class DiscoverState(
    val dapps: List<DApp> = emptyList(),
    val trendingTokens: List<TokenInfo> = emptyList(),
    val news: List<NewsItem> = emptyList(),
    val visitedDapps: List<String> = emptyList(),
    val dailyProgress: Int = 0,
    val isLoading: Boolean = false
)

data class NewsItem(
    val id: String,
    val title: String,
    val content: String,
    val source: String,
    val url: String,
    val publishedAt: LocalDateTime,
    val imageUrl: String?
) 