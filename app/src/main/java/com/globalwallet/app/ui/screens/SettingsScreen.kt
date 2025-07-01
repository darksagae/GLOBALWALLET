package com.globalwallet.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var isDarkTheme by remember { mutableStateOf(false) }
    var preferredCurrency by remember { mutableStateOf("USD") }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var mnemonic by remember { mutableStateOf("word1 word2 word3 ... word12") }
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
        Text("Settings", style = MaterialTheme.typography.headlineLarge, color = Color.White, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(12.dp))
        LiquidGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Security", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Biometric Authentication", color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = true, onCheckedChange = {})
                }
            }
        }
        LiquidGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Theme", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DarkMode, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dark Theme", color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    Switch(checked = isDarkTheme, onCheckedChange = { isDarkTheme = it })
                }
            }
        }
        LiquidGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Fiat Currency", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachMoney, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Preferred Currency", color = Color.White)
                    Spacer(modifier = Modifier.weight(1f))
                    DropdownMenu(expanded = false, onDismissRequest = {}) {
                        DropdownMenuItem(text = { Text("USD") }, onClick = { preferredCurrency = "USD" })
                        DropdownMenuItem(text = { Text("EUR") }, onClick = { preferredCurrency = "EUR" })
                    }
                    Text(preferredCurrency, color = Color.White)
                }
            }
        }
        LiquidGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Export Wallet", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassButton(onClick = { showExportDialog = true }) {
                    Text("Show Mnemonic", color = Color.White)
                }
            }
        }
        LiquidGlassCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Badges", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    UserBadgeItem(badgeType = BadgeType.SILVER_REFERRER)
                    Spacer(modifier = Modifier.width(8.dp))
                    UserBadgeItem(badgeType = BadgeType.SOCIALFI_CONTRIBUTOR)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        LiquidGlassButton(onClick = { showLogoutDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Logout", color = Color.White)
        }
    }
    if (showLogoutDialog) {
        LiquidGlassDialog(onDismissRequest = { showLogoutDialog = false }) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Are you sure you want to logout?", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel", color = Color.White)
                }
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassButton(onClick = { /* Clear session */ }, gradient = NeonPinkGradient) {
                    Text("Logout", color = Color.White)
                }
            }
        }
    }
    if (showExportDialog) {
        LiquidGlassDialog(onDismissRequest = { showExportDialog = false }) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.VpnKey, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Your Mnemonic Phrase", color = Color.White, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(mnemonic, color = Color.White, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LiquidGlassButton(onClick = { showExportDialog = false }) {
                    Text("Close", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun UserBadgeItem(badgeType: BadgeType) {
    LiquidGlassCard(modifier = Modifier.size(64.dp)) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = when (badgeType) {
                    BadgeType.SILVER_REFERRER -> Icons.Default.Star
                    BadgeType.SOCIALFI_CONTRIBUTOR -> Icons.Default.People
                    else -> Icons.Default.EmojiEvents
                },
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
        }
    }
} 