package com.globalwallet.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun TradeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) }
    var showConfirmation by remember { mutableStateOf(false) }
    var confirmationMessage by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val tabs = listOf("P2P Buy", "P2P Sell", "Swap", "Bridge", "Send", "Receive")

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
        Text(
            text = "Trade",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))
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
                            color = if (selectedTab == index) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.7f)
                        )
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        when (selectedTab) {
            0 -> P2PBuySection(onTradeComplete = { msg ->
                confirmationMessage = msg
                showConfirmation = true
            })
            1 -> P2PSellSection(onTradeComplete = { msg ->
                confirmationMessage = msg
                showConfirmation = true
            })
            2 -> SwapSection(onSwapComplete = { msg ->
                confirmationMessage = msg
                showConfirmation = true
            })
            3 -> BridgeSection(onBridgeComplete = { msg ->
                confirmationMessage = msg
                showConfirmation = true
            })
            4 -> SendSection(onSendComplete = { msg ->
                confirmationMessage = msg
                showConfirmation = true
            })
            5 -> ReceiveSection()
        }
    }
    if (showConfirmation) {
        LiquidGlassDialog(onDismissRequest = { showConfirmation = false }) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text(confirmationMessage, color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassButton(onClick = { showConfirmation = false }) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun P2PBuySection(onTradeComplete: (String) -> Unit) {
    val listings = listOf(
        P2PListing("1", "seller1", "ETH", BigDecimal("0.5"), BigDecimal("1800.00"), "Bank Transfer", P2PStatus.ACTIVE, LocalDateTime.now()),
        P2PListing("2", "seller2", "BNB", BigDecimal("2.0"), BigDecimal("500.00"), "PayPal", P2PStatus.ACTIVE, LocalDateTime.now())
    )
    Column {
        Text("P2P Buy Listings", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        listings.forEach { listing ->
            LiquidGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("${listing.tokenSymbol} - ${listing.amount}", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("${listing.price} USD", color = Color.White.copy(alpha = 0.7f))
                        Text("${listing.paymentMethod}", color = Color.White.copy(alpha = 0.7f))
                    }
                    LiquidGlassButton(onClick = {
                        onTradeComplete("Trade completed! +0.75 GLW reward")
                    }) {
                        Text("Buy", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun P2PSellSection(onTradeComplete: (String) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("") }
    Column {
        Text("Create Sell Listing", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = price, onValueChange = { price = it }, label = { Text("Price (USD)") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = paymentMethod, onValueChange = { paymentMethod = it }, label = { Text("Payment Method") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassButton(onClick = {
            onTradeComplete("Sell listing created! +0.75 GLW reward")
        }) {
            Text("Create Listing", color = Color.White)
        }
    }
}

@Composable
fun SwapSection(onSwapComplete: (String) -> Unit) {
    var fromToken by remember { mutableStateOf("BNB") }
    var toToken by remember { mutableStateOf("GLW") }
    var amount by remember { mutableStateOf("") }
    Column {
        Text("Swap Tokens", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = fromToken, onValueChange = { fromToken = it }, label = { Text("From Token") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = toToken, onValueChange = { toToken = it }, label = { Text("To Token") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassButton(onClick = {
            onSwapComplete("Swap completed! +0.75 GLW reward")
        }) {
            Text("Swap", color = Color.White)
        }
    }
}

@Composable
fun BridgeSection(onBridgeComplete: (String) -> Unit) {
    var fromChain by remember { mutableStateOf("Ethereum") }
    var toChain by remember { mutableStateOf("Polygon") }
    var amount by remember { mutableStateOf("") }
    Column {
        Text("Bridge Tokens", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = fromChain, onValueChange = { fromChain = it }, label = { Text("From Chain") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = toChain, onValueChange = { toChain = it }, label = { Text("To Chain") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassButton(onClick = {
            onBridgeComplete("Bridge completed! +0.75 GLW reward")
        }) {
            Text("Bridge", color = Color.White)
        }
    }
}

@Composable
fun SendSection(onSendComplete: (String) -> Unit) {
    var toAddress by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    Column {
        Text("Send Tokens", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = toAddress, onValueChange = { toAddress = it }, label = { Text("To Address") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassTextField(value = amount, onValueChange = { amount = it }, label = { Text("Amount") })
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassButton(onClick = {
            onSendComplete("Send completed! +0.75 GLW reward")
        }) {
            Text("Send", color = Color.White)
        }
    }
}

@Composable
fun ReceiveSection() {
    val address = "0xABCDEF1234567890"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Receive Tokens", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        LiquidGlassCard(modifier = Modifier.size(180.dp)) {
            // Placeholder for QR code
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.QrCode, contentDescription = null, tint = Color.White, modifier = Modifier.size(120.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(address, color = Color.White, style = MaterialTheme.typography.bodyMedium)
    }
} 