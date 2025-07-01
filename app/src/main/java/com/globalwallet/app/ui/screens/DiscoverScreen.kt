package com.globalwallet.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
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
import com.globalwallet.app.ui.components.LiquidGlassComponents.*
import com.globalwallet.app.data.model.*
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val dapps = listOf(
        DApp("1", "PancakeSwap", "DEX for BEP-20", "https://pancakeswap.finance", "bsc", DAppCategory.DEX, BigDecimal("0.3"), true),
        DApp("2", "QuickSwap", "DEX for Polygon", "https://quickswap.exchange", "polygon", DAppCategory.DEX, BigDecimal("0.3"), true),
        DApp("3", "Raydium", "DEX for Solana", "https://raydium.io", "solana", DAppCategory.DEX, BigDecimal("0.3"), true)
    )
    val trendingTokens = listOf(
        TokenInfo("GLW", "Global Wallet Token", 18, null, "bsc", BigDecimal("5.00"), null, null),
        TokenInfo("ETH", "Ethereum", 18, null, "ethereum", BigDecimal("3500.00"), null, null),
        TokenInfo("BNB", "Binance Coin", 18, null, "bsc", BigDecimal("600.00"), null, null)
    )
    val news = listOf(
        NewsItem("1", "GLW launches new mining feature!", "...", "CoinGecko", "https://coingecko.com", LocalDateTime.now(), null),
        NewsItem("2", "Polygon partners with Global Wallet", "...", "CoinGecko", "https://coingecko.com", LocalDateTime.now(), null)
    )
    var showDAppDialog by remember { mutableStateOf(false) }
    var selectedDApp by remember { mutableStateOf<DApp?>(null) }
    var showConfetti by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Column(
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
            .padding(16.dp)
    ) {
        Text("Discover", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        Text("DApps", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            dapps.forEach { dapp ->
                LiquidGlassCard(modifier = Modifier.size(120.dp).clickable {
                    selectedDApp = dapp
                    showDAppDialog = true
                    scope.launch {
                        showConfetti = true
                        delay(2000)
                        showConfetti = false
                    }
                }) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.Explore, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(dapp.name, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Trending Tokens", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(trendingTokens.size) { i ->
                val token = trendingTokens[i]
                LiquidGlassCard(modifier = Modifier.size(120.dp).padding(end = 8.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(token.symbol, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("$${token.price}", color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("News", style = MaterialTheme.typography.titleLarge, color = Color.White)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow {
            items(news.size) { i ->
                val item = news[i]
                LiquidGlassCard(modifier = Modifier.size(200.dp, 100.dp).padding(end = 8.dp)) {
                    Column(
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(item.title, color = Color.White, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(item.source, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
    if (showDAppDialog && selectedDApp != null) {
        LiquidGlassDialog(onDismissRequest = { showDAppDialog = false }) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Explore, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(selectedDApp!!.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Visit: ${selectedDApp!!.url}", color = Color.White, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassButton(onClick = { showDAppDialog = false }) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
    if (showConfetti) {
        Box(modifier = Modifier.fillMaxSize()) {
            LiquidConfetti(modifier = Modifier.fillMaxSize())
        }
    }
} 