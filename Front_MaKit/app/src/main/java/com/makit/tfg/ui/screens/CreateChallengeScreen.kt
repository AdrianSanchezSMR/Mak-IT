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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.makit.tfg.data.CategoryOption
import com.makit.tfg.ui.components.CategoryChip
import com.makit.tfg.ui.components.MakItTopBar
import com.makit.tfg.ui.components.PrimaryButton
import com.makit.tfg.ui.theme.MakCardBorder
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted
import com.makit.tfg.ui.theme.MakSurface

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CreateChallengeScreen(
    categories: List<CategoryOption>,
    isLoading: Boolean,
    onBack: () -> Unit,
    onSave: (categoriaId: Long, title: String, description: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var selectedCategoryId by rememberSaveable {
        mutableLongStateOf(categories.firstOrNull()?.id ?: 0L)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakSurface)
            .imePadding()
    ) {
        MakItTopBar(
            title = "Crear reto",
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
                text = "CREAR RETO",
                style = MaterialTheme.typography.labelLarge,
                color = MakGreen,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(26.dp))
            FieldLabel("NOMBRE DEL RETO")
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: Meditacion profunda") },
                singleLine = true,
                textStyle = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                colors = fieldColors()
            )
            Spacer(modifier = Modifier.height(24.dp))

            FieldLabel("DESCRIPCION")
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(126.dp),
                placeholder = { Text("Define el proposito de este reto...") },
                colors = fieldColors()
            )
            Spacer(modifier = Modifier.height(30.dp))

            FieldLabel("CATEGORIA")
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categories.forEach { cat ->
                    CategoryChip(
                        label = cat.name,
                        selected = selectedCategoryId == cat.id,
                        onClick = { selectedCategoryId = cat.id }
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        PrimaryButton(
            text = if (isLoading) "Guardando..." else "Crear reto",
            onClick = { onSave(selectedCategoryId, title, description) },
            enabled = !isLoading && title.isNotBlank() && selectedCategoryId > 0,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MakOnSurfaceMuted,
        fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MakGreen,
    unfocusedBorderColor = MakCardBorder,
    focusedLabelColor = MakGreen,
    cursorColor = MakGreen,
    focusedContainerColor = MakMintSoft,
    unfocusedContainerColor = MakMintSoft,
    focusedTextColor = MakGreenDark,
    unfocusedTextColor = MakGreenDark,
    focusedPlaceholderColor = MakOnSurfaceMuted,
    unfocusedPlaceholderColor = MakOnSurfaceMuted
)
