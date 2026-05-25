package com.example.studynotes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isCompleted: Boolean = false,
    val dueDate: Long? = null,
    val priority: Int = 1, // 0 = Low, 1 = Medium, 2 = High
    val category: String = "General",
    val noteId: Int? = null
)
