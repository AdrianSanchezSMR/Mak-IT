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
    profile: UserProfile,
    challenges: List<Challenge>,
    onViewAllChallenges: () -> Unit,
    onChangeReminder: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                        text = profile.initials,
                        style = MaterialTheme.typography.titleLarge,
                        color = MakMintSoft,
                        fontWeight = FontWeight.Bold
                    )
                }
                Column {
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = MakGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = profile.email,
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
                    value = profile.streakDays.toString(),
                    label = "Racha actual",
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = profile.completedCount.toString(),
                    label = "Completados",
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Configuración",
                style = MaterialTheme.typography.titleMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            SettingsRow(
                title = "Hora de aviso diario",
                value = profile.dailyReminderHour,
                action = "Cambiar",
                onAction = onChangeReminder
            )
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Categorías activas en el sorteo",
                style = MaterialTheme.typography.titleMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                profile.activeCategories.take(2).forEach { cat ->
                    CategoryChip(
                        label = cat.label,
                        selected = true,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                profile.activeCategories.drop(2).take(2).forEach { cat ->
                    CategoryChip(
                        label = cat.label,
                        selected = true,
                        onClick = {},
                        modifier = Modifier.weight(1f)
                    )
                }
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
                TextButton(onClick = onViewAllChallenges) {
                    Text("Ver todos →", color = MakGreenLight)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            challenges.filter { it.isActive }.take(2).forEach { challenge ->
                ChallengeListItem(challenge = challenge)
                Spacer(modifier = Modifier.height(8.dp))
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
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = challenge.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MakGreenDark,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${challenge.category.label} · ${challenge.difficulty.label} · activo",
                    style = MaterialTheme.typography.bodySmall,
                    color = MakOnSurfaceMuted
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MakOnSurfaceMuted
            )
        }
    }
}
