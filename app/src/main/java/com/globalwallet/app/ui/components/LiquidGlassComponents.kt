package com.globalwallet.app.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 3D Liquid Glass UI Components
 * 
 * Features:
 * - Glassmorphism effects with translucent backgrounds
 * - Liquid animations with flowing ripples and morphing shapes
 * - Neon blue/purple gradients for futuristic feel
 * - Interactive 3D touch responses with depth effects
 * - Pulsating buttons with z-axis depth
 */

// Neon gradient brushes for 3D Liquid Glass effects
val NeonBlueGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF00D4FF),
        Color(0xFF0099CC),
        Color(0xFF00D4FF)
    )
)

val NeonPurpleGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFF8B5CF6),
        Color(0xFF6D28D9),
        Color(0xFF8B5CF6)
    )
)

val NeonPinkGradient = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEC4899),
        Color(0xFFBE185D),
        Color(0xFFEC4899)
    )
)

val GlassGradient = Brush.linearGradient(
    colors = listOf(
        Color(0x1AFFFFFF),
        Color(0x0DFFFFFF),
        Color(0x1AFFFFFF)
    )
)

@Composable
fun LiquidGlassCard(
    modifier: Modifier = Modifier,
    gradient: Brush = GlassGradient,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_glass")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(gradient)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0x33FFFFFF),
                        Color(0x66FFFFFF),
                        Color(0x33FFFFFF)
                    )
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0xFF00D4FF).copy(alpha = 0.3f)
            )
    ) {
        content()
    }
}

@Composable
fun LiquidGlassButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    gradient: Brush = NeonBlueGradient,
    content: @Composable RowScope.() -> Unit
) {
    val scope = rememberCoroutineScope()
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_button")
    
    val pulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )
    
    val ripple by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ripple"
    )
    
    Button(
        onClick = {
            scope.launch {
                // Add haptic feedback simulation
                delay(100)
            }
            onClick()
        },
        modifier = modifier
            .graphicsLayer {
                scaleX = pulse
                scaleY = pulse
                alpha = if (enabled) 1f else 0.6f
            },
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .clip(RoundedCornerShape(12.dp))
        ) {
            // Ripple effect
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        alpha = (1f - ripple) * 0.3f
                        scaleX = 1f + ripple * 0.2f
                        scaleY = 1f + ripple * 0.2f
                    }
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
            
            // Content
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}

@Composable
fun LiquidGlassDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        LiquidGlassCard(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            gradient = GlassGradient
        ) {
            content()
        }
    }
}

@Composable
fun LiquidProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    gradient: Brush = NeonBlueGradient
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_progress")
    
    val flow by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "flow"
    )
    
    Box(
        modifier = modifier
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color(0x1AFFFFFF))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
                .graphicsLayer {
                    translationX = flow * 20f
                }
                .background(gradient)
                .clip(RoundedCornerShape(4.dp))
        )
    }
}

@Composable
fun LiquidGlassTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    isError: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_text_field")
    
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )
    
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(GlassGradient)
            .border(
                width = 1.dp,
                brush = if (isError) {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFFEF4444),
                            Color(0xFFDC2626)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0x33FFFFFF),
                            Color(0x66FFFFFF)
                        )
                    )
                },
                shape = RoundedCornerShape(12.dp)
            )
            .graphicsLayer {
                alpha = 0.9f + shimmer * 0.1f
            },
        label = label,
        placeholder = placeholder,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            focusedLabelColor = Color(0xFF00D4FF),
            unfocusedLabelColor = Color.White.copy(alpha = 0.7f),
            textColor = Color.White,
            placeholderColor = Color.White.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun LiquidGlassIcon(
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    gradient: Brush = NeonPurpleGradient
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_icon")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
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
    
    Box(
        modifier = modifier
            .size(48.dp)
            .graphicsLayer {
                rotationZ = rotation
                alpha = glow
            }
            .clip(RoundedCornerShape(12.dp))
            .background(gradient)
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0x66FFFFFF),
                        Color(0x33FFFFFF)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
fun LiquidConfetti(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "liquid_confetti")
    
    val particles = (0..20).map { index ->
        val delay = index * 100
        val duration = 2000 + (index % 3) * 500
        
        val y by infiniteTransition.animateFloat(
            initialValue = -100f,
            targetValue = 1000f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delayMillis = delay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "y_$index"
        )
        
        val x by infiniteTransition.animateFloat(
            initialValue = -50f + (index % 10) * 10f,
            targetValue = 50f + (index % 10) * 10f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delayMillis = delay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "x_$index"
        )
        
        val rotation by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delayMillis = delay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "rotation_$index"
        )
        
        Triple(x, y, rotation)
    }
    
    Box(modifier = modifier) {
        particles.forEachIndexed { index, (x, y, rotation) ->
            val color = when (index % 3) {
                0 -> Color(0xFF00D4FF)
                1 -> Color(0xFF8B5CF6)
                else -> Color(0xFFEC4899)
            }
            
            Box(
                modifier = Modifier
                    .offset(x = x.dp, y = y.dp)
                    .size(4.dp)
                    .graphicsLayer {
                        rotationZ = rotation
                        alpha = if (y < 0f || y > 800f) 0f else 0.8f
                    }
                    .background(color, RoundedCornerShape(2.dp))
            )
        }
    }
} 