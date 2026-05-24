package com.makit.tfg.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makit.tfg.data.CategoryOption
import com.makit.tfg.ui.components.CategoryChip
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.components.PrimaryButton
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InterestsScreen(
    categories: List<CategoryOption>,
    selectedIds: Set<Long>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSave: (Set<Long>) -> Unit,
    modifier: Modifier = Modifier
) {
    var selection by remember(selectedIds) { mutableStateOf(selectedIds) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakSurface)
            .imePadding()
    ) {
        MakItTopBar(
            title = "Mis intereses",
            showBack = true,
            showLogo = true,
            onBack = onBack
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "Elige las categorías para el sorteo diario",
                style = MaterialTheme.typography.bodyLarge,
                color = MakGreenDark
            )
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    CategoryChip(
                        label = category.name,
                        selected = category.id in selection,
                        onClick = {
                            selection = if (category.id in selection) {
                                selection - category.id
                            } else {
                                selection + category.id
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = "Guardar intereses",
                onClick = { onSave(selection.toSet()) },
                enabled = !isLoading && selection.isNotEmpty()
            )
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
