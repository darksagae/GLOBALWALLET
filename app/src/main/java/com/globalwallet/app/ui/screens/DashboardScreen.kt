package com.globalwallet.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.globalwallet.app.ui.components.LiquidGlassComponents
import com.globalwallet.app.ui.components.LiquidGlassComponents.*
import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Dashboard Screen for Global Wallet App
 * 
 * Features:
 * - 3D Liquid Glass balance card with rotation animation
 * - Interactive mining section with 12-hour cooldown
 * - Recent transactions with liquid ripple effects
 * - Side-edge scrollable network selection
 * - Streak bonuses and gamification elements
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var totalBalance by remember { mutableStateOf(BigDecimal("1250.75")) }
    var currentStreak by remember { mutableStateOf(3) }
    var lastMiningTime by remember { mutableStateOf(LocalDateTime.now().minusHours(8)) }
    var isMiningAvailable by remember { mutableStateOf(true) }
    var showConfetti by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    // Mock data
    val balances = remember {
        listOf(
            Balance("1", "wallet1", "ETH", "Ethereum", BigDecimal("2.5"), BigDecimal("4500.00"), "ethereum", LocalDateTime.now()),
            Balance("2", "wallet1", "BNB", "Binance Coin", BigDecimal("5.2"), BigDecimal("1200.00"), "bsc", LocalDateTime.now()),
            Balance("3", "wallet1", "GLW", "Global Wallet Token", BigDecimal("150.0"), BigDecimal("750.00"), "bsc", LocalDateTime.now()),
            Balance("4", "wallet1", "MATIC", "Polygon", BigDecimal("1000.0"), BigDecimal("800.00"), "polygon", LocalDateTime.now())
        )
    }
    
    val recentTransactions = remember {
        listOf(
            Transaction("1", "wallet1", "0x123...", "0xabc...", "0xdef...", BigDecimal("0.1"), "ETH", BigDecimal("0.005"), TransactionStatus.CONFIRMED, TransactionType.SEND, "ethereum", LocalDateTime.now().minusHours(2), 12345678),
            Transaction("2", "wallet1", "0x456...", "0xghi...", "0xabc...", BigDecimal("1.5"), "GLW", null, TransactionStatus.CONFIRMED, TransactionType.MINING_REWARD, "bsc", LocalDateTime.now().minusHours(8), 12345679),
            Transaction("3", "wallet1", "0x789...", "0xjkl...", "0xabc...", BigDecimal("0.05"), "BNB", BigDecimal("0.001"), TransactionStatus.CONFIRMED, TransactionType.RECEIVE, "bsc", LocalDateTime.now().minusDays(1), 12345680)
        )
    }
    
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0A0A0A),
                        Color(0xFF1A1A1A),
                        Color(0xFF0A0A0A)
                    )
                )
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            // Header
            Text(
                text = "Global Wallet",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            // Balance Card with 3D Liquid Glass effect
            BalanceCard(
                totalBalance = totalBalance,
                balances = balances,
                onCardTap = {
                    // Trigger 3D rotation animation
                }
            )
        }
        
        item {
            // Mining Section
            MiningSection(
                currentStreak = currentStreak,
                lastMiningTime = lastMiningTime,
                isMiningAvailable = isMiningAvailable,
                onMiningClick = {
                    scope.launch {
                        // Simulate mining process
                        isMiningAvailable = false
                        delay(2000) // Mining animation duration
                        
                        // Award mining reward
                        val baseReward = BigDecimal("1.5")
                        val streakBonus = if (currentStreak > 0) BigDecimal("0.5") else BigDecimal.ZERO
                        val totalReward = baseReward + streakBonus
                        
                        totalBalance = totalBalance.add(totalReward)
                        currentStreak++
                        lastMiningTime = LocalDateTime.now()
                        showConfetti = true
                        
                        delay(3000) // Show confetti
                        showConfetti = false
                        
                        // Set 12-hour cooldown
                        delay(1000)
                        isMiningAvailable = false
                    }
                }
            )
        }
        
        item {
            // Recent Transactions
            RecentTransactionsSection(transactions = recentTransactions)
        }
    }
    
    // Confetti overlay
    if (showConfetti) {
        Box(modifier = Modifier.fillMaxSize()) {
            LiquidConfetti(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun BalanceCard(
    totalBalance: BigDecimal,
    balances: List<Balance>,
    onCardTap: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "balance_card")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val glow by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )
    
    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .graphicsLayer {
                rotationZ = rotation
                alpha = glow
            }
            .clickable { onCardTap() },
        gradient = Brush.linearGradient(
            colors = listOf(
                Color(0x1A00D4FF),
                Color(0x0D8B5CF6),
                Color(0x1AEC4899)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Total Balance",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$${totalBalance.setScale(2, java.math.RoundingMode.HALF_UP)}",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Network balances preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                balances.take(3).forEach { balance ->
                    NetworkBalanceItem(balance = balance)
                }
            }
        }
    }
}

@Composable
fun NetworkBalanceItem(balance: Balance) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = balance.tokenSymbol,
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.7f)
        )
        
        Text(
            text = balance.amount.setScale(2, java.math.RoundingMode.HALF_UP).toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun MiningSection(
    currentStreak: Int,
    lastMiningTime: LocalDateTime,
    isMiningAvailable: Boolean,
    onMiningClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "mining_section")
    
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        gradient = Brush.linearGradient(
            colors = listOf(
                Color(0x1A8B5CF6),
                Color(0x0DEC4899),
                Color(0x1A00D4FF)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Mining",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    
                    Text(
                        text = "Streak: $currentStreak days",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
                
                LiquidGlassButton(
                    onClick = onMiningClick,
                    enabled = isMiningAvailable,
                    modifier = Modifier
                        .graphicsLayer {
                            scaleX = if (isMiningAvailable) pulse else 1f
                            scaleY = if (isMiningAvailable) pulse else 1f
                        },
                    gradient = if (isMiningAvailable) {
                        NeonBlueGradient
                    } else {
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0x66FFFFFF),
                                Color(0x33FFFFFF)
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Mining",
                        tint = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Mining progress indicator
            if (!isMiningAvailable) {
                val timeSinceLastMining = java.time.Duration.between(lastMiningTime, LocalDateTime.now())
                val progress = (timeSinceLastMining.toHours() / 12.0).coerceIn(0.0, 1.0)
                
                LiquidProgressBar(
                    progress = progress.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "Next mining in ${12 - timeSinceLastMining.toHours()} hours",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun RecentTransactionsSection(transactions: List<Transaction>) {
    Column {
        Text(
            text = "Recent Transactions",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        transactions.forEach { transaction ->
            TransactionItem(transaction = transaction)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val infiniteTransition = rememberInfiniteTransition(label = "transaction_item")
    
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    LiquidGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .graphicsLayer {
                alpha = 0.9f + shimmer * 0.1f
            },
        gradient = GlassGradient
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Transaction icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        when (transaction.type) {
                            TransactionType.SEND -> Color(0xFFEF4444)
                            TransactionType.RECEIVE -> Color(0xFF10B981)
                            TransactionType.MINING_REWARD -> Color(0xFF8B5CF6)
                            else -> Color(0xFF6B7280)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (transaction.type) {
                        TransactionType.SEND -> Icons.Default.Send
                        TransactionType.RECEIVE -> Icons.Default.Download
                        TransactionType.MINING_REWARD -> Icons.Default.PlayArrow
                        else -> Icons.Default.SwapHoriz
                    },
                    contentDescription = null,
                    tint = Color.White
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Transaction details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${transaction.amount} ${transaction.tokenSymbol}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = transaction.type.name.replace("_", " "),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            
            // Transaction status and time
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = when (transaction.status) {
                        TransactionStatus.CONFIRMED -> "✓"
                        TransactionStatus.PENDING -> "⏳"
                        TransactionStatus.FAILED -> "✗"
                        else -> "?"
                    },
                    style = MaterialTheme.typography.titleMedium,
                    color = when (transaction.status) {
                        TransactionStatus.CONFIRMED -> Color(0xFF10B981)
                        TransactionStatus.PENDING -> Color(0xFFF59E0B)
                        TransactionStatus.FAILED -> Color(0xFFEF4444)
                        else -> Color(0xFF6B7280)
                    }
                )
                
                Text(
                    text = transaction.timestamp.format(DateTimeFormatter.ofPattern("HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
} 