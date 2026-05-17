package com.makit.tfg.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
import com.makit.tfg.ui.components.CategoryChip
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted

@Composable
fun ProfileScreen(
    profile: UserProfile?,
    challenges: List<Challenge>,
    onViewAllChallenges: () -> Unit,
    onEditInterests: () -> Unit,
    onChangeReminder: () -> Unit,
    onLogout: () -> Unit,
    onCreateReto: () -> Unit,
    modifier: Modifier = Modifier
) {
    val user = profile ?: return
    var showAllChallenges by remember { mutableStateOf(false) }
    val activeChallenges = challenges.filter { it.isActive }
    val visibleChallenges = if (showAllChallenges) activeChallenges else activeChallenges.take(2)
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakMintSoft)
    ) {
        MakItTopBar(title = "Mi perfil", showLogo = true)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MakGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.initials,
                        style = MaterialTheme.typography.titleLarge,
                        color = MakMintSoft,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MakGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MakOnSurfaceMuted
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    value = user.streakDays.toString(),
                    label = "Racha actual",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = user.completedCount.toString(),
                    label = "Completados",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Configuracion",
                style = MaterialTheme.typography.titleMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsRow(
                title = "Hora de aviso diario",
                value = user.dailyReminderHour,
                action = "Cambiar",
                onAction = onChangeReminder
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Categorias activas en el sorteo",
                style = MaterialTheme.typography.titleMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            if (user.activeCategoryNames.isEmpty()) {
                Text(
                    text = "Sin categorias seleccionadas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MakOnSurfaceMuted
                )
            } else {
                user.activeCategoryNames.chunked(2).forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        row.forEach { name ->
                            CategoryChip(
                                label = name,
                                selected = true,
                                onClick = {},
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = onEditInterests,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Elegir categorias del sorteo", color = MakGreen)
            }
            Spacer(modifier = Modifier.height(28.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mis retos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MakGreenDark,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(
                    onClick = {
                        showAllChallenges = !showAllChallenges
                        onViewAllChallenges()
                    },
                    enabled = activeChallenges.size > 2
                ) {
                    Text(
                        if (showAllChallenges) "Ver menos" else "Ver todos",
                        color = MakGreenLight
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (activeChallenges.isEmpty()) {
                Text(
                    text = "Todavia no tienes retos activos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MakOnSurfaceMuted
                )
            }
            visibleChallenges.forEach { challenge ->
                ChallengeListItem(challenge = challenge)
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(
                onClick = onCreateReto,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Crear nuevo reto", color = MakGreen)
            }
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(onClick = onLogout, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Cerrar sesion", color = MakGreenLight)
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MakMint
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MakOnSurfaceMuted
            )
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    value: String,
    action: String,
    onAction: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = MakMintSoft,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = title, style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium,
                    color = MakGreenDark,
                    fontWeight = FontWeight.SemiBold
                )
            }
            TextButton(onClick = onAction) {
                Text(action, color = MakGreen)
            }
        }
    }
}

@Composable
private fun ChallengeListItem(challenge: Challenge) {
    val statusText = if (challenge.isCompletedToday) "completado hoy" else "pendiente"
    val statusColor = if (challenge.isCompletedToday) MakGreen else MakOnSurfaceMuted
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        color = if (challenge.isCompletedToday) MakMint else MakMintSoft,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MakGreenDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${challenge.categoryName} - $statusText",
                    style = MaterialTheme.typography.bodySmall,
                    color = statusColor
                )
            }
            Icon(
                imageVector = if (challenge.isCompletedToday) {
                    Icons.Default.CheckCircle
                } else {
                    Icons.AutoMirrored.Filled.KeyboardArrowRight
                },
                contentDescription = null,
                tint = statusColor
            )
        }
    }
}
