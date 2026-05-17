package com.makit.tfg.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authDataStore by preferencesDataStore(name = "makit_auth")

class AuthTokenStore(private val context: Context) {

    private val tokenKey = stringPreferencesKey("jwt_token")

    val tokenFlow: Flow<String?> = context.authDataStore.data.map { prefs ->
        prefs[tokenKey]
    }

    suspend fun getToken(): String? = tokenFlow.first()

    suspend fun saveToken(token: String) {
        context.authDataStore.edit { prefs ->
            prefs[tokenKey] = token
        }
    }

    suspend fun clear() {
        context.authDataStore.edit { prefs ->
            prefs.remove(tokenKey)
        }
    }
}
