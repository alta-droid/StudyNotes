package com.example.studynotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: Int = 0xFF6200EE.toInt(),
    val isPinned: Boolean = false,
    val isCornell: Boolean = false,
    val cueColumn: String = "",
    val summaryColumn: String = "",
    val category: String = "General"
)
