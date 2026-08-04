package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class ReadingStatus(val labelPt: String) {
    READING("Lendo"),
    WANT_TO_READ("Quero Ler"),
    READ("Lido");

    companion object {
        fun fromString(value: String): ReadingStatus {
            return entries.firstOrNull { 
                it.name.equals(value, ignoreCase = true) || it.labelPt.equals(value, ignoreCase = true) 
            } ?: READING
        }
    }
}

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val author: String,
    val totalPages: Int,
    val currentPage: Int = 0,
    val status: String = ReadingStatus.READING.name,
    val coverUrl: String = "",
    val category: String = "Geral",
    val rating: Float = 0f,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis()
) {
    val progressPercentage: Int
        get() = if (totalPages > 0) {
            ((currentPage.toFloat() / totalPages.toFloat()) * 100).toInt().coerceIn(0, 100)
        } else 0

    val statusEnum: ReadingStatus
        get() = ReadingStatus.fromString(status)
}
