package com.globalwallet.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.globalwallet.app.ui.components.LiquidGlassComponents
import com.globalwallet.app.ui.components.LiquidGlassComponents.*
import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Earn Screen for Global Wallet App
 * 
 * Features:
 * - Referral system with tiered rewards
 * - Daily tasks with GLW token rewards
 * - SocialFi integration for Web3 social activities
 * - Mystery box rewards and limited-time events
 * - 3D Liquid Glass UI with interactive animations
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EarnScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var totalEarned by remember { mutableStateOf(BigDecimal("45.75")) }
    var totalReferrals by remember { mutableStateOf(7) }
    var currentTier by remember { mutableStateOf(ReferralTier.SILVER) }
    var showConfetti by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) }
    
    val scope = rememberCoroutineScope()
    
    val tabs = listOf("Referrals", "Tasks", "SocialFi", "Events")
    
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
                text = "Earn GLW Tokens",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        
        item {
            // Total Earned Card
            TotalEarnedCard(totalEarned = totalEarned)
        }
        
        item {
            // Tab Navigation
            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.fillMaxWidth(),
                containerColor = Color.Transparent,
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                color = if (selectedTab == index) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color.White.copy(alpha = 0.7f)
                                }
                            )
                        }
                    )
                }
            }
        }
        
        when (selectedTab) {
            0 -> {
                item {
                    ReferralsSection(
                        totalReferrals = totalReferrals,
                        currentTier = currentTier,
                        onReferralComplete = { reward ->
                            scope.launch {
                                totalEarned = totalEarned.add(reward)
                                totalReferrals++
                                showConfetti = true
                                delay(3000)
                                showConfetti = false
                            }
                        }
                    )
                }
            }
            1 -> {
                item {
                    TasksSection(
                        onTaskComplete = { reward ->
                            scope.launch {
                                totalEarned = totalEarned.add(reward)
                                showConfetti = true
                                delay(3000)
                                showConfetti = false
                            }
                        }
                    )
                }
            }
            2 -> {
                item {
                    SocialFiSection(
                        onSocialActivityComplete = { reward ->
                            scope.launch {
                                totalEarned = totalEarned.add(reward)
                                showConfetti = true
                                delay(3000)
                                showConfetti = false
                            }
                        }
                    )
                }
            }
            3 -> {
                item {
                    EventsSection(
                        onEventComplete = { reward ->
                            scope.launch {
                                totalEarned = totalEarned.add(reward)
                                showConfetti = true
                                delay(3000)
                                showConfetti = false
                            }
                        }
                    )
                }
            }
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
fun TotalEarnedCard(totalEarned: BigDecimal) {
    val infiniteTransition = rememberInfiniteTransition(label = "total_earned")
    
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
            .height(120.dp)
            .graphicsLayer {
                alpha = glow
            },
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
                text = "Total Earned",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "${totalEarned.setScale(2, java.math.RoundingMode.HALF_UP)} GLW",
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ReferralsSection(
    totalReferrals: Int,
    currentTier: ReferralTier,
    onReferralComplete: (BigDecimal) -> Unit
) {
    val referralCode = "GLWABC123"
    val nextTierProgress = when (currentTier) {
        ReferralTier.BRONZE -> (totalReferrals / 5.0).coerceIn(0.0, 1.0)
        ReferralTier.SILVER -> ((totalReferrals - 5) / 5.0).coerceIn(0.0, 1.0)
        ReferralTier.GOLD -> ((totalReferrals - 10) / 10.0).coerceIn(0.0, 1.0)
        ReferralTier.PLATINUM -> 1.0
    }
    
    Column {
        Text(
            text = "Invite Friends",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Referral Code Card
        LiquidGlassCard(
            modifier = Modifier.fillMaxWidth(),
            gradient = GlassGradient
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Your Referral Code",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = referralCode,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    LiquidGlassButton(
                        onClick = { /* Copy to clipboard */ },
                        gradient = NeonBlueGradient
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Code",
                            tint = Color.White
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Share this code with friends to earn rewards!",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Referral Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ReferralStatCard(
                title = "Total Referrals",
                value = totalReferrals.toString(),
                gradient = NeonBlueGradient
            )
            
            ReferralStatCard(
                title = "Current Tier",
                value = currentTier.name,
                gradient = NeonPurpleGradient
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Progress to next tier
        if (currentTier != ReferralTier.PLATINUM) {
            Column {
                Text(
                    text = "Progress to ${getNextTier(currentTier).name}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LiquidProgressBar(
                    progress = nextTierProgress.toFloat(),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "${getReferralsNeeded(currentTier)} more referrals needed",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ReferralStatCard(
    title: String,
    value: String,
    gradient: Brush
) {
    LiquidGlassCard(
        modifier = Modifier
            .weight(1f)
            .height(80.dp),
        gradient = gradient
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.8f)
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TasksSection(onTaskComplete: (BigDecimal) -> Unit) {
    val tasks = remember {
        listOf(
            TaskItem("Watch Video", "3 GLW", "Watch a 10-second video", EarnActivityType.VIDEO_WATCH, BigDecimal("3.0")),
            TaskItem("P2P Trade", "4 GLW", "Complete a P2P trade", EarnActivityType.P2P_TRADE, BigDecimal("4.0")),
            TaskItem("Social Share", "1.5 GLW", "Share on social media", EarnActivityType.SOCIAL_SHARE, BigDecimal("1.5")),
            TaskItem("Daily Login", "15 GLW", "Login for 7 consecutive days", EarnActivityType.DAILY_LOGIN, BigDecimal("15.0")),
            TaskItem("Profile Setup", "5 GLW", "Complete your profile", EarnActivityType.PROFILE_SETUP, BigDecimal("5.0"))
        )
    }
    
    Column {
        Text(
            text = "Daily Tasks",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        tasks.forEach { task ->
            TaskCard(
                task = task,
                onComplete = { onTaskComplete(task.reward) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun TaskCard(
    task: TaskItem,
    onComplete: () -> Unit
) {
    var isCompleted by remember { mutableStateOf(false) }
    
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradient = if (isCompleted) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0x1A10B981),
                    Color(0x0D10B981)
                )
            )
        } else {
            GlassGradient
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Text(
                    text = task.rewardText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (!isCompleted) {
                LiquidGlassButton(
                    onClick = {
                        isCompleted = true
                        onComplete()
                    },
                    gradient = NeonBlueGradient
                ) {
                    Text(
                        text = "Complete",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun SocialFiSection(onSocialActivityComplete: (BigDecimal) -> Unit) {
    Column {
        Text(
            text = "SocialFi Activities",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Create Content
        SocialFiCard(
            title = "Create Content",
            description = "Post crypto-related updates",
            reward = "2 GLW",
            onComplete = { onSocialActivityComplete(BigDecimal("2.0")) }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Join Communities
        SocialFiCard(
            title = "Join Communities",
            description = "Join Web3 communities",
            reward = "3 GLW",
            onComplete = { onSocialActivityComplete(BigDecimal("3.0")) }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Social Challenges
        SocialFiCard(
            title = "Social Challenges",
            description = "Invite 3 friends to a community",
            reward = "5 GLW",
            onComplete = { onSocialActivityComplete(BigDecimal("5.0")) }
        )
    }
}

@Composable
fun SocialFiCard(
    title: String,
    description: String,
    reward: String,
    onComplete: () -> Unit
) {
    var isCompleted by remember { mutableStateOf(false) }
    
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradient = if (isCompleted) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0x1A8B5CF6),
                    Color(0x0D8B5CF6)
                )
            )
        } else {
            GlassGradient
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Text(
                    text = reward,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.Bold
                )
            }
            
            if (!isCompleted) {
                LiquidGlassButton(
                    onClick = {
                        isCompleted = true
                        onComplete()
                    },
                    gradient = NeonPurpleGradient
                ) {
                    Text(
                        text = "Complete",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun EventsSection(onEventComplete: (BigDecimal) -> Unit) {
    Column {
        Text(
            text = "Limited-Time Events",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Weekly Challenge
        EventCard(
            title = "Weekly Challenge",
            description = "Swap 0.01 BNB for GLW on BEP-20",
            reward = "8 GLW",
            timeLeft = "2 days left",
            onComplete = { onEventComplete(BigDecimal("8.0")) }
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Mystery Box
        EventCard(
            title = "Mystery Box",
            description = "Unlock after 5 weekly tasks",
            reward = "2-20 GLW",
            timeLeft = "Available",
            onComplete = { onEventComplete(BigDecimal("15.0")) }
        )
    }
}

@Composable
fun EventCard(
    title: String,
    description: String,
    reward: String,
    timeLeft: String,
    onComplete: () -> Unit
) {
    var isCompleted by remember { mutableStateOf(false) }
    
    LiquidGlassCard(
        modifier = Modifier.fillMaxWidth(),
        gradient = if (isCompleted) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0x1AEC4899),
                    Color(0x0DEC4899)
                )
            )
        } else {
            GlassGradient
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
                
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f)
                )
                
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = reward,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = timeLeft,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            
            if (!isCompleted) {
                LiquidGlassButton(
                    onClick = {
                        isCompleted = true
                        onComplete()
                    },
                    gradient = NeonPinkGradient
                ) {
                    Text(
                        text = "Complete",
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Completed",
                    tint = Color(0xFFEC4899),
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

data class TaskItem(
    val title: String,
    val rewardText: String,
    val description: String,
    val type: EarnActivityType,
    val reward: BigDecimal
)

fun getNextTier(currentTier: ReferralTier): ReferralTier {
    return when (currentTier) {
        ReferralTier.BRONZE -> ReferralTier.SILVER
        ReferralTier.SILVER -> ReferralTier.GOLD
        ReferralTier.GOLD -> ReferralTier.PLATINUM
        ReferralTier.PLATINUM -> ReferralTier.PLATINUM
    }
}

fun getReferralsNeeded(currentTier: ReferralTier): Int {
    return when (currentTier) {
        ReferralTier.BRONZE -> 5
        ReferralTier.SILVER -> 5
        ReferralTier.GOLD -> 10
        ReferralTier.PLATINUM -> 0
    }
} 