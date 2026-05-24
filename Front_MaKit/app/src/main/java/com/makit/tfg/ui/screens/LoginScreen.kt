package com.makit.tfg.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.makit.tfg.ui.components.OutlineButton
import com.makit.tfg.ui.components.PrimaryButton
import com.makit.tfg.ui.components.SectionDivider
import com.makit.tfg.ui.theme.MakCardBorder
import com.makit.tfg.ui.theme.MakGreen
import com.makit.tfg.ui.theme.MakGreenDark
import com.makit.tfg.ui.theme.MakMint
import com.makit.tfg.ui.theme.MakMintSoft
import com.makit.tfg.ui.theme.MakOnSurfaceMuted
import com.makit.tfg.ui.theme.MakSurface

@Composable
fun LoginScreen(
    isLoading: Boolean,
    onLogin: (username: String, password: String) -> Unit,
    onCreateAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MakSurface)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 18.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MakMint,
            border = BorderStroke(1.dp, MakCardBorder)
        ) {
            Box(
                modifier = Modifier.padding(horizontal = 28.dp, vertical = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "M",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MakGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
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
        Spacer(modifier = Modifier.height(44.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MakCardBorder, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = MakMintSoft
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    text = "USUARIO",
                    style = MaterialTheme.typography.labelLarge,
                    color = MakOnSurfaceMuted,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = { Text("nombre@ejemplo.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = fieldColors()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "CONTRASEÑA",
                    style = MaterialTheme.typography.labelLarge,
                    color = MakOnSurfaceMuted,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = fieldColors()
                )
                Spacer(modifier = Modifier.height(28.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        color = MakGreen,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    PrimaryButton(
                        text = "Entrar",
                        onClick = { onLogin(username, password) },
                        enabled = username.isNotBlank() && password.isNotBlank()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                SectionDivider()
                Spacer(modifier = Modifier.height(24.dp))
                OutlineButton(text = "Crear cuenta nueva", onClick = onCreateAccount)
            }
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MakGreen,
    unfocusedBorderColor = MakCardBorder,
    focusedLabelColor = MakGreen,
    cursorColor = MakGreen,
    focusedContainerColor = MakSurface,
    unfocusedContainerColor = MakSurface,
    focusedTextColor = MakGreenDark,
    unfocusedTextColor = MakGreenDark,
    focusedPlaceholderColor = MakOnSurfaceMuted,
    unfocusedPlaceholderColor = MakOnSurfaceMuted
)
