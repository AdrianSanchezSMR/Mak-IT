package com.makit.tfg.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.makit.tfg.R
import com.makit.tfg.ui.theme.MakCardBorder
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted
import com.makit.tfg.ui.theme.MakStreak

enum class BottomNavItem(val label: String, val icon: ImageVector) {
    Inicio("Inicio", Icons.Default.Home),
    Stats("Stats", Icons.Default.BarChart),
    Perfil("Perfil", Icons.Default.Person)
}

@Composable
fun MakItLogo(
    modifier: Modifier = Modifier,
    compact: Boolean = false,
    logoSize: Dp? = null,
    showWordmark: Boolean = true
) {
    val boxSize = logoSize ?: if (compact) 36.dp else 44.dp
    val corner = if (compact) 8.dp else if (boxSize >= 56.dp) 14.dp else 10.dp
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.makit_logo),
            contentDescription = "Logo Mak-IT",
            modifier = Modifier
                .size(boxSize)
                .clip(RoundedCornerShape(corner)),
            contentScale = ContentScale.Fit
        )
        if (showWordmark) {
            Text(
                text = "Mak-IT",
                style = if (compact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MakItTopBar(
    title: String? = null,
    showBack: Boolean = false,
    onBack: () -> Unit = {},
    showLogo: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBack) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = MakGreenDark
                )
            }
        }
        if (showLogo) {
            MakItLogo(compact = title != null)
        }
        if (title != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MakGreenDark,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MakItBottomBar(
    selected: BottomNavItem,
    onItemSelected: (BottomNavItem) -> Unit,
    onFabClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shadowElevation = 12.dp,
        color = MakMintSoft
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BottomNavItem.entries.forEach { item ->
                val selectedItem = item == selected
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onItemSelected(item) }
                        .padding(vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selectedItem) MakGreen else MakOnSurfaceMuted
                    )
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelMedium,
                        color = if (selectedItem) MakGreen else MakOnSurfaceMuted
                    )
                }
            }
            if (onFabClick != null) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(MakGreen)
                        .clickable(onClick = onFabClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Nuevo reto",
                        tint = MakMintSoft
                    )
                }
            }
        }
    }
}

@Composable
fun StreakBadge(days: Int, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = MakMint
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Whatshot,
                contentDescription = null,
                tint = MakStreak,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "$days días de racha",
                style = MaterialTheme.typography.labelLarge,
                color = MakGreenDark
            )
        }
    }
}

@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (selected) MakGreen else MakMintSoft
    val borderColor = if (selected) MakGreen else MakCardBorder
    val textColor = if (selected) MakMintSoft else MakGreenDark

    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
        color = background,
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
    }
}

@Composable
fun DifficultyChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CategoryChip(label = label, selected = selected, onClick = onClick, modifier = modifier)
}

@Composable
fun SectionDivider(text: String = "o", modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MakCardBorder)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MakOnSurfaceMuted
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MakCardBorder)
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(enabled = enabled, onClick = onClick),
        color = if (enabled) MakGreen else MakCardBorder,
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MakMintSoft,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun OutlineButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .border(1.5.dp, MakGreenLight, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        color = MakMintSoft,
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = MakGreen,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
