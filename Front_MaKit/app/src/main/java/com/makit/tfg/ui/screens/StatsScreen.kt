package com.makit.tfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makit.tfg.data.UserProfile
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted

@Composable
fun StatsScreen(
    profile: UserProfile,
    weeklyProgress: Float = 0.7f,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakMintSoft)
    ) {
        MakItTopBar(title = "Stats", showLogo = true)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Tu progreso",
                style = MaterialTheme.typography.headlineMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            StatOverviewCard(
                title = "Retos completados",
                value = profile.completedCount.toString(),
                subtitle = "Total histórico"
            )
            StatOverviewCard(
                title = "Racha actual",
                value = "${profile.streakDays} días",
                subtitle = "¡Sigue así!"
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MakMint
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Progreso semanal",
                        style = MaterialTheme.typography.titleMedium,
                        color = MakGreenDark,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { weeklyProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = MakGreen,
                        trackColor = MakMintSoft
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${(weeklyProgress * 100).toInt()}% de la meta semanal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MakOnSurfaceMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun StatOverviewCard(title: String, value: String, subtitle: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MakMintSoft,
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, color = MakOnSurfaceMuted)
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
            Text(text = subtitle, style = MaterialTheme.typography.labelMedium, color = MakGreenLight)
        }
    }
}
