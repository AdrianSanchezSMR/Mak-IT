package com.makit.tfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.makit.tfg.data.MakItRepository
import com.makit.tfg.navigation.MakItNavHost
import com.makit.tfg.ui.theme.MakITTheme
import com.makit.tfg.ui.theme.MakMintSoft

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val repository = remember { MakItRepository(applicationContext) }
            MakITTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MakMintSoft
                ) {
                    MakItNavHost(repository = repository)
                }
            }
        }
    }
}
