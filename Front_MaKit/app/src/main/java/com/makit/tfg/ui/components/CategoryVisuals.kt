package com.makit.tfg.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalFlorist
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Recycling
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.makit.tfg.ui.theme.MakGreen

data class CategoryVisual(
    val color: Color,
    val icon: ImageVector
)

fun categoryVisual(categoryName: String): CategoryVisual {
    val normalized = categoryName.lowercase()
    return when {
        normalized.contains("deporte") || normalized.contains("sport") || normalized.contains("gym") || normalized.contains("fitness") -> CategoryVisual(Color(0xFF7ED957), Icons.Default.FitnessCenter)
        normalized.contains("salud") || normalized.contains("bienestar") || normalized.contains("wellness") -> CategoryVisual(Color(0xFF8E8CFF), Icons.Default.SelfImprovement)
        normalized.contains("estudio") || normalized.contains("aprendiz") || normalized.contains("educ") || normalized.contains("school") -> CategoryVisual(Color(0xFF5BC0EB), Icons.Default.School)
        normalized.contains("trabajo") || normalized.contains("work") || normalized.contains("carrera") -> CategoryVisual(Color(0xFFFFA94D), Icons.Default.Work)
        normalized.contains("arte") || normalized.contains("dise") || normalized.contains("draw") || normalized.contains("creativ") -> CategoryVisual(Color(0xFFFF8DA1), Icons.Default.Brush)
        normalized.contains("musica") || normalized.contains("music") -> CategoryVisual(Color(0xFFB39DFF), Icons.Default.MusicNote)
        normalized.contains("comida") || normalized.contains("aliment") || normalized.contains("nutric") || normalized.contains("food") -> CategoryVisual(Color(0xFFFFB84D), Icons.Default.Restaurant)
        normalized.contains("hogar") || normalized.contains("casa") || normalized.contains("home") -> CategoryVisual(Color(0xFF6BCB77), Icons.Default.Home)
        normalized.contains("social") || normalized.contains("amig") || normalized.contains("people") || normalized.contains("equipo") -> CategoryVisual(Color(0xFFFF7B7B), Icons.Default.People)
        normalized.contains("natur") || normalized.contains("eco") || normalized.contains("medioamb") || normalized.contains("green") -> CategoryVisual(Color(0xFF59C98E), Icons.Default.LocalFlorist)
        normalized.contains("mente") || normalized.contains("mind") || normalized.contains("medit") || normalized.contains("yoga") -> CategoryVisual(Color(0xFF9B87FF), Icons.Default.SelfImprovement)
        normalized.contains("tecn") || normalized.contains("code") || normalized.contains("program") || normalized.contains("tech") -> CategoryVisual(Color(0xFF57A6FF), Icons.Default.Code)
        normalized.contains("viaj") || normalized.contains("travel") || normalized.contains("mundo") -> CategoryVisual(Color(0xFF4DD0E1), Icons.Default.Public)
        normalized.contains("dorm") || normalized.contains("descans") || normalized.contains("sleep") -> CategoryVisual(Color(0xFF6A6B8D), Icons.Default.Bedtime)
        normalized.contains("lect") || normalized.contains("book") || normalized.contains("leer") -> CategoryVisual(Color(0xFF3ECF8E), Icons.Default.Book)
        normalized.contains("reto") || normalized.contains("challenge") -> CategoryVisual(MakGreen, Icons.Default.EmojiEvents)
        else -> defaultVisualFor(categoryName)
    }
}

fun defaultVisualFor(categoryName: String): CategoryVisual {
    val palette = listOf(
        CategoryVisual(Color(0xFF7C83FD), Icons.Default.AutoAwesome),
        CategoryVisual(Color(0xFFFFB347), Icons.Default.LocalFireDepartment),
        CategoryVisual(Color(0xFF68D391), Icons.Default.Spa),
        CategoryVisual(Color(0xFFFF7E67), Icons.Default.DirectionsRun),
        CategoryVisual(Color(0xFF5BC0EB), Icons.Default.Public),
        CategoryVisual(Color(0xFFB39DFF), Icons.Default.Star),
        CategoryVisual(Color(0xFFFF9F1C), Icons.Default.Recycling),
        CategoryVisual(Color(0xFF70C1B3), Icons.Default.LocalFlorist),
        CategoryVisual(Color(0xFFFF6B6B), Icons.Default.Favorite),
        CategoryVisual(Color(0xFF9C89FF), Icons.Default.Diamond)
    )
    val index = kotlin.math.abs(categoryName.lowercase().hashCode()) % palette.size
    return palette[index]
}
