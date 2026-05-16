package com.makit.tfg.data

enum class ChallengeCategory(val label: String) {
    Mindfulness("Mindfulness"),
    Lectura("Lectura"),
    Tecnologia("Tecnología"),
    Fitness("Fitness"),
    Creatividad("Creatividad"),
    Nutricion("Nutrición")
}

enum class Difficulty(val label: String) {
    Facil("Fácil"),
    Media("Media"),
    Dificil("Difícil")
}

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val category: ChallengeCategory,
    val difficulty: Difficulty,
    val durationMinutes: Int,
    val isActive: Boolean = true,
    val isToday: Boolean = false
)

data class UserProfile(
    val name: String,
    val email: String,
    val initials: String,
    val streakDays: Int,
    val completedCount: Int,
    val dailyReminderHour: String = "08:00",
    val activeCategories: Set<ChallengeCategory> = setOf(
        ChallengeCategory.Mindfulness,
        ChallengeCategory.Creatividad,
        ChallengeCategory.Fitness,
        ChallengeCategory.Lectura
    )
)
