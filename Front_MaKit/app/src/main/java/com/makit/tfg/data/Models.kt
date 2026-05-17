package com.makit.tfg.data

data class UserProfile(
    val username: String,
    val email: String,
    val role: String,
    val streakDays: Int,
    val completedCount: Int,
    val dailyReminderHour: String,
    val activeCategoryIds: Set<Long> = emptySet(),
    val activeCategoryNames: List<String> = emptyList()
) {
    val name: String get() = username
    val initials: String get() = username.take(2).uppercase()
}

data class Challenge(
    val id: Long,
    val title: String,
    val description: String,
    val categoryName: String,
    val categoryId: Long,
    val isCompletedToday: Boolean = false,
    val isActive: Boolean = true
)

data class CategoryOption(
    val id: Long,
    val name: String
)

fun formatHoraAviso(raw: String?): String {
    if (raw.isNullOrBlank()) return "Sin configurar"
    return raw.take(5)
}
