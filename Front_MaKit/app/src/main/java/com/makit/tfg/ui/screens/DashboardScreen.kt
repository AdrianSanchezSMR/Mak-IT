package com.makit.tfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Spa
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
            .background(MakSurface)
    ) {
        MakItTopBar(showLogo = true)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "PANEL DE CONTROL",
                style = MaterialTheme.typography.labelLarge,
                color = MakOnSurfaceMuted,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Hola de nuevo, ${user.name}",
                style = MaterialTheme.typography.headlineSmall,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                repeat(7) { index ->
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .clip(CircleShape)
                            .background(if (index < user.streakDays.coerceAtMost(7)) MakGreen else MakCardBorder)
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            if (!hasChallenges && !isLoading) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    color = MakMintSoft,
                    border = BorderStroke(1.dp, MakCardBorder)
                ) {
                    Text(
                        text = "No tienes retos pendientes. Los que ya completaste estan en Perfil.",
                        modifier = Modifier.padding(24.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MakOnSurfaceMuted
                    )
                }
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
                Spacer(modifier = Modifier.height(16.dp))
            }

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
                    color = MakGreen,
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                StatTile(
                    value = user.streakDays.toString(),
                    label = "Dias enfocados",
                    iconTint = MakStreak,
                    modifier = Modifier.weight(1f)
                )
                StatTile(
                    value = user.completedCount.toString(),
                    label = "Retos logrados",
                    iconTint = MakGreen,
                    modifier = Modifier.weight(1f)
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
    val date = formatAssignedDate(challenge.assignedDate)
    val statusText = when {
        challenge.isCompletedToday -> "Completado"
        date != null -> "Pendiente - $date"
        else -> "Pendiente"
    }
    val borderColor = if (isSelected) MakGreen else MakCardBorder

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(enabled = !challenge.isCompletedToday, onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        color = MakMintSoft,
        shadowElevation = if (isSelected) 8.dp else 2.dp
    ) {
        Column(modifier = Modifier.padding(26.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = MakGreen, modifier = Modifier.size(20.dp))
                    Text(
                        text = challenge.categoryName.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MakGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                }
                Surface(shape = RoundedCornerShape(10.dp), color = MakMint) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MakGreen
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = challenge.title,
                style = MaterialTheme.typography.displaySmall,
                color = MakGreenDark,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = challenge.description.ifBlank { "Completa este reto para seguir avanzando en tu rutina." },
                style = MaterialTheme.typography.bodyLarge,
                color = MakOnSurfaceMuted
            )
            if (challenge.isCompletedToday) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MakGreen, modifier = Modifier.size(20.dp))
                    Text("Completado", style = MaterialTheme.typography.labelLarge, color = MakGreen)
                }
            }
        }
    }
}

@Composable
private fun StatTile(
    value: String,
    label: String,
    iconTint: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.height(142.dp),
        shape = RoundedCornerShape(18.dp),
        color = MakMintSoft,
        border = BorderStroke(1.dp, MakCardBorder)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = if (iconTint == MakStreak) Icons.Default.LocalFireDepartment else Icons.Default.Spa,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(value, style = MaterialTheme.typography.headlineMedium, color = MakGreenDark, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MakOnSurfaceMuted)
        }
    }
}
