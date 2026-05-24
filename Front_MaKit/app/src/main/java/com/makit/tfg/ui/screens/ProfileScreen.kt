package com.makit.tfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Notifications
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
import com.makit.tfg.data.CategoryOption
import com.makit.tfg.data.Challenge
import com.makit.tfg.data.UserProfile
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.components.categoryVisual
import com.makit.tfg.ui.theme.MakAccentOrange
import com.makit.tfg.ui.theme.MakCardBorder
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted
import com.makit.tfg.ui.theme.MakSurface

@Composable
fun ProfileScreen(
    profile: UserProfile?,
    categories: List<CategoryOption>,
    selectedInterestIds: Set<Long>,
    challenges: List<Challenge>,
    onToggleInterest: (Long, Boolean) -> Unit,
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

            Spacer(modifier = Modifier.height(24.dp))

            Text("Categorias activas", style = MaterialTheme.typography.titleLarge, color = MakGreenDark, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            if (categories.isEmpty()) {
                Text("No hay categorias disponibles.", style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(end = 8.dp)
                ) {
                    items(categories, key = { it.id }) { category ->
                        val visual = categoryVisual(category.name)
                        val selected = category.id in selectedInterestIds
                        CategoryToggleCard(
                            name = category.name,
                            selected = selected,
                            color = visual.color,
                            icon = visual.icon,
                            onToggle = { onToggleInterest(category.id, it) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mis retos", style = MaterialTheme.typography.titleLarge, color = MakGreenDark, fontWeight = FontWeight.Bold)
                TextButton(
                    onClick = { showAllChallenges = !showAllChallenges },
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

@Composable
private fun CategoryToggleCard(
    name: String,
    selected: Boolean,
    color: androidx.compose.ui.graphics.Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onToggle: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onToggle(!selected) },
        shape = RoundedCornerShape(999.dp),
        color = if (selected) color.copy(alpha = 0.20f) else MakCardBorder.copy(alpha = 0.55f),
        border = BorderStroke(1.5.dp, if (selected) color else MakCardBorder)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .width(185.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            }
            Text(
                text = name,
                color = if (selected) MakGreenDark else MakOnSurfaceMuted,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = if (selected) "Activa" else "Inactiva",
                color = if (selected) color else MakOnSurfaceMuted,
                style = MaterialTheme.typography.labelMedium
            )
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
    val visual = categoryVisual(challenge.categoryName)
    val date = formatAssignedDate(challenge.assignedDate)
    val statusText = when {
        challenge.isCompletedToday && date != null -> "COMPLETADO - $date"
        challenge.isCompletedToday -> "COMPLETADO"
        date != null -> "PENDIENTE - $date"
        else -> "PENDIENTE"
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        color = MakMintSoft,
        border = BorderStroke(1.dp, visual.color)
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
                        .background(visual.color.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (challenge.isCompletedToday) Icons.Default.CheckCircle else visual.icon,
                        contentDescription = null,
                        tint = visual.color,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Surface(shape = RoundedCornerShape(8.dp), color = visual.color.copy(alpha = 0.16f)) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = visual.color,
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
            Text(challenge.categoryName, style = MaterialTheme.typography.labelLarge, color = visual.color)
        }
    }
}
