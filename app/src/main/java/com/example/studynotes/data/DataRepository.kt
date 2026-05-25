package com.example.studynotes.data

import kotlinx.coroutines.flow.Flow

interface DataRepository {
    fun getAllNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun insertNote(note: Note): Long
    suspend fun deleteNote(note: Note)
    fun getCategories(): Flow<List<String>>
    fun getNotesCount(): Flow<Int>

    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: Int): Task?
    suspend fun insertTask(task: Task): Long
    suspend fun deleteTask(task: Task)
    fun getTasksCount(): Flow<Int>
    fun getCompletedTasksCount(): Flow<Int>
}

class DefaultDataRepository(
    private val noteDao: NoteDao,
    private val taskDao: TaskDao
) : DataRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    override suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)
    override suspend fun insertNote(note: Note): Long = noteDao.insertNote(note)
    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    override fun getCategories(): Flow<List<String>> = noteDao.getCategories()
    override fun getNotesCount(): Flow<Int> = noteDao.getNotesCountFlow()

    override fun getAllTasks(): Flow<List<Task>> = taskDao.getAllTasks()
    override suspend fun getTaskById(id: Int): Task? = taskDao.getTaskById(id)
    override suspend fun insertTask(task: Task): Long = taskDao.insertTask(task)
    override suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)
    override fun getTasksCount(): Flow<Int> = taskDao.getTasksCountFlow()
    override fun getCompletedTasksCount(): Flow<Int> = taskDao.getCompletedTasksCountFlow()
}
