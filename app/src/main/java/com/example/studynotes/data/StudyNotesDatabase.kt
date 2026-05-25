package com.example.studynotes.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Task::class], version = 1, exportSchema = false)
abstract class StudyNotesDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: StudyNotesDatabase? = null

        fun getDatabase(context: Context): StudyNotesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StudyNotesDatabase::class.java,
                    "study_notes_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
