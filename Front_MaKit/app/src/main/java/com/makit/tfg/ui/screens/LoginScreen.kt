package com.makit.tfg.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makit.tfg.ui.components.MakItLogo
import com.makit.tfg.ui.components.OutlineButton
import com.makit.tfg.ui.components.PrimaryButton
import com.makit.tfg.ui.components.SectionDivider
import com.makit.tfg.ui.theme.MakCardBorder
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import androidx.compose.ui.text.font.FontWeight
import com.makit.tfg.ui.theme.MakGreenLight
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted

@Composable
fun LoginScreen(
    onLogin: (email: String, password: String) -> Unit,
    onCreateAccount: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("tu@email.com") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 28.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        MakItLogo(logoSize = 88.dp, showWordmark = false)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Mak-IT",
            style = MaterialTheme.typography.headlineMedium,
            color = MakGreenDark,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Tus retos, tu ritmo",
            style = MaterialTheme.typography.bodyLarge,
            color = MakOnSurfaceMuted,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = fieldColors()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            placeholder = { Text("•••••") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = fieldColors()
        )
        Spacer(modifier = Modifier.height(28.dp))

        PrimaryButton(
            text = "Entrar",
            onClick = { onLogin(email, password) },
            enabled = email.isNotBlank()
        )
        Spacer(modifier = Modifier.height(24.dp))
        SectionDivider()
        Spacer(modifier = Modifier.height(24.dp))

        OutlineButton(text = "Crear cuenta nueva", onClick = onCreateAccount)
        Spacer(modifier = Modifier.height(20.dp))

        TextButton(onClick = onForgotPassword) {
            Text(
                text = "¿Olvidaste la contraseña? Recupérala",
                style = MaterialTheme.typography.bodyMedium,
                color = MakGreenLight,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MakGreen,
    unfocusedBorderColor = MakCardBorder,
    focusedLabelColor = MakGreen,
    cursorColor = MakGreen,
    focusedContainerColor = MakMintSoft,
    unfocusedContainerColor = MakMintSoft
)
