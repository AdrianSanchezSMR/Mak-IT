package com.makit.tfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makit.tfg.data.UserProfile
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.theme.MakAccentOrange
import com.makit.tfg.ui.theme.MakCardBorder
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted
import com.makit.tfg.ui.theme.MakSurface
import com.makit.tfg.ui.theme.MakStreak
import kotlin.math.roundToInt

@Composable
fun StatsScreen(
    profile: UserProfile?,
    weeklyProgress: Float = 0f,
    activeTasksCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val user = profile ?: return
    val progress = weeklyProgress.coerceIn(0f, 1f)
    val percent = (progress * 100).roundToInt()
    val remainingTasks = activeTasksCount.coerceAtLeast(0)
    val focusedDays = user.streakDays
    val completedChallenges = user.completedCount
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakSurface)
    ) {
        MakItTopBar(title = "Stats", showLogo = true)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProgressHeroCard(
                percent = percent,
                remainingTasks = remainingTasks,
                completedChallenges = completedChallenges
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.EmojiEvents,
                    iconTint = MakStreak,
                    value = focusedDays.toString(),
                    label = "Días enfocados"
                )
                MetricCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    iconTint = MakGreen,
                    value = completedChallenges.toString(),
                    label = "Retos logrados"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ProgressHeroCard(
    percent: Int,
    remainingTasks: Int,
    completedChallenges: Int
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(30.dp),
        color = MakMintSoft,
        border = BorderStroke(1.dp, MakCardBorder)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PROGRESO DIARIO",
                    style = MaterialTheme.typography.labelLarge,
                    color = MakOnSurfaceMuted,
                    fontWeight = FontWeight.Bold
                )
                Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MakAccentOrange)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = { percent / 100f },
                    modifier = Modifier.size(128.dp),
                    strokeWidth = 10.dp,
                    color = MakGreen,
                    trackColor = MakCardBorder
                )
                Text(
                    text = "$percent%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MakGreenDark,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = if (remainingTasks == 1) {
                    "Estás a 1 tarea de cumplir tu meta diaria."
                } else {
                    "Estás a $remainingTasks tareas de cumplir tu meta diaria."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MakOnSurfaceMuted,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Llevas $completedChallenges retos completados este mes.",
                style = MaterialTheme.typography.labelLarge,
                color = MakGreenLight,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun MetricCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    value: String,
    label: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        color = MakMintSoft,
        border = BorderStroke(1.dp, MakCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(28.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, color = MakGreenDark, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
        }
    }
}
