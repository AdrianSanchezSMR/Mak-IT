package com.makit.tfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Notifications
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
import com.makit.tfg.ui.components.CategoryChip
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

@OptIn(ExperimentalLayoutApi::class)
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
    val profileChallenges = challenges.filter { it.isActive }
    val visibleChallenges = if (showAllChallenges) profileChallenges else profileChallenges.take(4)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakSurface)
    ) {
        MakItTopBar(title = "Mi perfil", showLogo = true)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = MakMintSoft,
                border = BorderStroke(1.dp, MakCardBorder)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MakMint),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user.initials,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MakGreen,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(user.name, style = MaterialTheme.typography.headlineSmall, color = MakGreenDark, fontWeight = FontWeight.Bold)
                    Text(user.email, style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        StatCard(value = "${user.streakDays} dias", label = "Racha actual", modifier = Modifier.weight(1f))
                        StatCard(value = "${user.completedCount} retos", label = "Completados", modifier = Modifier.weight(1f))
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                color = MakMintSoft,
                border = BorderStroke(1.dp, MakCardBorder)
            ) {
                Column(modifier = Modifier.padding(22.dp)) {
                    Text("Ajustes", style = MaterialTheme.typography.titleLarge, color = MakGreenDark, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(14.dp))
                    SettingsRow(
                        title = "Notificaciones",
                        value = user.dailyReminderHour,
                        onAction = onChangeReminder
                    )
                }
            }
            Spacer(modifier = Modifier.height(28.dp))

            Text("Categorias activas", style = MaterialTheme.typography.titleLarge, color = MakGreenDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            if (user.activeCategoryNames.isEmpty()) {
                Text("Sin categorias seleccionadas.", style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
            } else {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    user.activeCategoryNames.forEach { name ->
                        CategoryChip(label = name, selected = true, onClick = {})
                    }
                }
            }
            TextButton(onClick = onEditInterests, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Elegir categorias del sorteo", color = MakGreen)
            }
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mis retos", style = MaterialTheme.typography.titleLarge, color = MakGreenDark, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = {
                        showAllChallenges = !showAllChallenges
                        onViewAllChallenges()
                    },
                    enabled = profileChallenges.size > 4
                ) {
                    Text(if (showAllChallenges) "Ver menos" else "Ver todos", color = MakGreenLight)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (profileChallenges.isEmpty()) {
                Text("Todavia no tienes retos asignados.", style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
            }
            visibleChallenges.forEach { challenge ->
                ChallengeListItem(challenge = challenge)
                Spacer(modifier = Modifier.height(14.dp))
            }
            TextButton(onClick = onCreateReto, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Crear nuevo reto", color = MakGreen)
            }
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
        shape = RoundedCornerShape(14.dp),
        color = MakMint
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleLarge, color = MakGreenDark, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.labelSmall, color = MakOnSurfaceMuted)
        }
    }
}

@Composable
private fun SettingsRow(
    title: String,
    value: String,
    onAction: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = MakGreen, modifier = Modifier.size(20.dp))
            Text(title, style = MaterialTheme.typography.bodyMedium, color = MakGreenDark, fontWeight = FontWeight.SemiBold)
        }
        Surface(shape = RoundedCornerShape(8.dp), color = MakMint) {
            TextButton(onClick = onAction) {
                Text(value, color = MakGreenDark)
            }
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
private fun ChallengeListItem(challenge: Challenge) {
    val date = formatAssignedDate(challenge.assignedDate)
    val statusText = when {
        challenge.isCompletedToday && date != null -> "COMPLETADO - $date"
        challenge.isCompletedToday -> "COMPLETADO"
        date != null -> "PENDIENTE - $date"
        else -> "PENDIENTE"
    }
    val statusColor = if (challenge.isCompletedToday) MakGreen else MakAccentOrange
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MakMintSoft,
        border = BorderStroke(1.dp, if (challenge.isCompletedToday) MakCardBorder else MakAccentOrange)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(MakMint),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (challenge.isCompletedToday) Icons.Default.CheckCircle else Icons.Default.Spa,
                        contentDescription = null,
                        tint = statusColor,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Surface(shape = RoundedCornerShape(8.dp), color = MakMint) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(challenge.title, style = MaterialTheme.typography.headlineSmall, color = MakGreenDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = challenge.description.ifBlank { challenge.categoryName },
                style = MaterialTheme.typography.bodyMedium,
                color = MakOnSurfaceMuted
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(challenge.categoryName, style = MaterialTheme.typography.labelLarge, color = MakGreen)
        }
    }
}
