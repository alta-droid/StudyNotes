package com.example.studynotes.ui.main

import com.example.studynotes.data.DataRepository
import com.example.studynotes.data.Note
import com.example.studynotes.data.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class MainScreenViewModelTest {
    @Test
    fun testNotesFlow_emitsCorrectData() = runTest {
        val fakeRepo = FakeDataRepository()
        val viewModel = MainScreenViewModel(fakeRepo)
        assertEquals(viewModel.notes.value, emptyList<Note>()) // Initial value is empty
        assertEquals(viewModel.notes.first(), fakeRepo.notesList)
    }
}

private class FakeDataRepository : DataRepository {
    val notesList = listOf(Note(id = 1, title = "Test Note", content = "Content"))
    val tasksList = listOf(Task(id = 1, title = "Test Task"))

    override fun getAllNotes(): Flow<List<Note>> = flowOf(notesList)
    override suspend fun getNoteById(id: Int): Note? = notesList.find { it.id == id }
    override suspend fun insertNote(note: Note): Long = 1L
    override suspend fun deleteNote(note: Note) {}
    override fun getCategories(): Flow<List<String>> = flowOf(listOf("General"))
    override fun getNotesCount(): Flow<Int> = flowOf(notesList.size)

    override fun getAllTasks(): Flow<List<Task>> = flowOf(tasksList)
    override suspend fun getTaskById(id: Int): Task? = tasksList.find { it.id == id }
    override suspend fun insertTask(task: Task): Long = 1L
    override suspend fun deleteTask(task: Task) {}
    override fun getTasksCount(): Flow<Int> = flowOf(tasksList.size)
    override fun getCompletedTasksCount(): Flow<Int> = flowOf(0)
}
