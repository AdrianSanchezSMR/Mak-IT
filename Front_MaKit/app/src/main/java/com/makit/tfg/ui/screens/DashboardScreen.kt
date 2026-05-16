package com.makit.tfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makit.tfg.data.Challenge
import com.makit.tfg.data.UserProfile
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.components.PrimaryButton
import com.makit.tfg.ui.components.StreakBadge
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted

@Composable
fun DashboardScreen(
    profile: UserProfile,
    todayChallenge: Challenge?,
    onCompleteCheckIn: () -> Unit,
    onViewAllChallenges: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakMintSoft)
    ) {
        MakItTopBar(showLogo = true)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Hola de nuevo,",
                style = MaterialTheme.typography.bodyLarge,
                color = MakOnSurfaceMuted
            )
            Text(
                text = profile.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            StreakBadge(days = profile.streakDays)
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Reto de hoy",
                style = MaterialTheme.typography.titleMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            todayChallenge?.let { challenge ->
                TodayChallengeCard(challenge = challenge)
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(text = "Check-in completado", onClick = onCompleteCheckIn)
            Spacer(modifier = Modifier.height(12.dp))
            TextButton(
                onClick = onViewAllChallenges,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Ver todos mis retos",
                    color = MakGreenLight,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TodayChallengeCard(challenge: Challenge) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MakMintSoft,
        shadowElevation = 2.dp,
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MakMint
                ) {
                    Text(
                        text = challenge.category.label,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MakGreenDark
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MakGreen.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "Sorteado hoy",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MakGreen
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.titleLarge,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = challenge.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MakOnSurfaceMuted
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = MakGreenLight,
                        modifier = Modifier.height(18.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "${challenge.durationMinutes} min",
                        style = MaterialTheme.typography.labelLarge,
                        color = MakGreenDark
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MakGreenLight,
                        modifier = Modifier.height(18.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Dificultad ${challenge.difficulty.label.lowercase()}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MakGreenDark
                    )
                }
            }
        }
    }
}
