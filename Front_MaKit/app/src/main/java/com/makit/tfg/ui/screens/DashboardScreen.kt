package com.makit.tfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    profile: UserProfile?,
    todayChallenges: List<Challenge>,
    isLoading: Boolean,
    onCompleteCheckIn: (Challenge) -> Unit,
    onViewAllChallenges: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user = profile ?: return
    val hasChallenges = todayChallenges.isNotEmpty()
    var selectedChallengeId by remember(todayChallenges) {
        mutableStateOf(todayChallenges.firstOrNull { !it.isCompletedToday }?.id)
    }
    val selectedChallenge = todayChallenges.firstOrNull {
        it.id == selectedChallengeId && !it.isCompletedToday
    } ?: todayChallenges.firstOrNull { !it.isCompletedToday }

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
                text = user.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            StreakBadge(days = user.streakDays)
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Retos pendientes",
                style = MaterialTheme.typography.titleMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (!hasChallenges && !isLoading) {
                Text(
                    text = "No tienes retos pendientes. Los que ya completaste estan en Perfil.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MakOnSurfaceMuted
                )
            }

            todayChallenges.forEach { challenge ->
                TodayChallengeCard(
                    challenge = challenge,
                    isSelected = challenge.id == selectedChallenge?.id,
                    onClick = {
                        if (!challenge.isCompletedToday) {
                            selectedChallengeId = challenge.id
                        }
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = when {
                    isLoading -> "Cargando..."
                    selectedChallenge != null -> "Marcar check-in"
                    else -> "Sin retos pendientes"
                },
                onClick = { selectedChallenge?.let(onCompleteCheckIn) },
                enabled = !isLoading && selectedChallenge != null
            )
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

private fun formatAssignedDate(raw: String?): String? {
    if (raw.isNullOrBlank() || raw.length < 10) return null
    val parts = raw.substring(0, 10).split("-")
    if (parts.size != 3) return null
    return "${parts[2]}/${parts[1]}/${parts[0]}"
}

@Composable
private fun TodayChallengeCard(
    challenge: Challenge,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        challenge.isCompletedToday -> MakGreen.copy(alpha = 0.35f)
        isSelected -> MakGreen
        else -> MakMint
    }
    val statusText = when {
        challenge.isCompletedToday -> "Completado"
        isSelected -> "Seleccionado"
        else -> {
            val date = formatAssignedDate(challenge.assignedDate)
            if (date != null) "Pendiente - $date" else "Pendiente"
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, borderColor, RoundedCornerShape(20.dp))
            .clickable(enabled = !challenge.isCompletedToday, onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        color = MakMintSoft,
        shadowElevation = if (isSelected) 5.dp else 2.dp,
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
                        text = challenge.categoryName,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = MakGreenDark
                    )
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) MakGreen.copy(alpha = 0.16f) else MakGreen.copy(alpha = 0.10f)
                ) {
                    Text(
                        text = statusText,
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
            if (challenge.isCompletedToday) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MakGreen,
                        modifier = Modifier.height(18.dp)
                    )
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(
                        text = "Completado hoy",
                        style = MaterialTheme.typography.labelLarge,
                        color = MakGreen
                    )
                }
            }
        }
    }
}
