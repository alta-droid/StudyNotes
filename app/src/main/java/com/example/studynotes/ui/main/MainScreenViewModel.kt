package com.example.studynotes.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studynotes.data.DataRepository
import com.example.studynotes.data.Note
import com.example.studynotes.data.Task
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardStats(
    val totalNotes: Int = 0,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val completionPercentage: Float = 0f
)

class MainScreenViewModel(private val repository: DataRepository) : ViewModel() {

    val notes: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val tasks: StateFlow<List<Task>> = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<String>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats: StateFlow<DashboardStats> = combine(
        repository.getNotesCount(),
        repository.getTasksCount(),
        repository.getCompletedTasksCount()
    ) { notesCount, tasksCount, completedCount ->
        val percentage = if (tasksCount > 0) {
            (completedCount.toFloat() / tasksCount.toFloat()) * 100
        } else {
            0f
        }
        DashboardStats(notesCount, tasksCount, completedCount, percentage)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardStats())

    fun saveNote(note: Note) {
        viewModelScope.launch {
            repository.insertNote(note)
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun saveTask(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }

    fun toggleTaskCompleted(task: Task) {
        viewModelScope.launch {
            repository.insertTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
