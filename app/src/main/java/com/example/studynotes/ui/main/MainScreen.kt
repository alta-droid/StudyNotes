package com.example.studynotes.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import com.example.studynotes.data.DefaultDataRepository
import com.example.studynotes.data.Note
import com.example.studynotes.data.StudyNotesDatabase
import com.example.studynotes.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onItemClick: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainScreenViewModel = viewModel {
        val context = LocalContext.current
        val db = StudyNotesDatabase.getDatabase(context)
        MainScreenViewModel(DefaultDataRepository(db.noteDao(), db.taskDao()))
    },
) {
    // Collecting flows from view model
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()
    val stats by viewModel.stats.collectAsStateWithLifecycle()

    // Screen states
    var selectedTab by remember { mutableIntStateOf(0) } // 0=Dashboard, 1=Notes, 2=Tasks
    var selectedNote by remember { mutableStateOf<Note?>(null) }
    var isCreatingNote by remember { mutableStateOf(false) }
    var isCornellNew by remember { mutableStateOf(false) }

    if (selectedNote != null || isCreatingNote) {
        // Render Note editor screen
        NoteDetailScreen(
            note = selectedNote,
            isCornellInitial = isCornellNew,
            onSave = { note ->
                viewModel.saveNote(note)
                selectedNote = null
                isCreatingNote = false
            },
            onBack = {
                selectedNote = null
                isCreatingNote = false
            }
        )
    } else {
        // Main Screen layout with bottom nav
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = when (selectedTab) {
                                0 -> "لوحة الإنجاز الدراسية"
                                1 -> "دفتر المحاضرات والدروس"
                                else -> "قائمة المهام والواجبات"
                            },
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        icon = { Icon(Icons.Default.Dashboard, contentDescription = "لوحة التحكم") },
                        label = { Text("الرئيسية", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        icon = { Icon(Icons.Default.Book, contentDescription = "الملاحظات") },
                        label = { Text("الدروس", fontSize = 11.sp) }
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        icon = { Icon(Icons.Default.ListAlt, contentDescription = "المهام") },
                        label = { Text("المهام", fontSize = 11.sp) }
                    )
                }
            },
            modifier = modifier.fillMaxSize()
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (selectedTab) {
                    0 -> {
                        DashboardScreen(
                            stats = stats,
                            onQuickAction = { action ->
                                when (action) {
                                    "new_cornell" -> {
                                        isCornellNew = true
                                        isCreatingNote = true
                                    }
                                    "new_task" -> {
                                        selectedTab = 2
                                    }
                                }
                            }
                        )
                    }
                    1 -> {
                        NotesScreen(
                            notes = notes,
                            categories = categories,
                            onNoteClick = { note ->
                                selectedNote = note
                            },
                            onDeleteNote = { note ->
                                viewModel.deleteNote(note)
                            },
                            onPinToggle = { note ->
                                viewModel.saveNote(note.copy(isPinned = !note.isPinned))
                            },
                            onCreateNote = { isCornell ->
                                isCornellNew = isCornell
                                selectedNote = null
                                isCreatingNote = true
                            }
                        )
                    }
                    2 -> {
                        TasksScreen(
                            tasks = tasks,
                            onSaveTask = { task ->
                                viewModel.saveTask(task)
                            },
                            onToggleTask = { task ->
                                viewModel.toggleTaskCompleted(task)
                            },
                            onDeleteTask = { task ->
                                viewModel.deleteTask(task)
                            }
                        )
                    }
                }
            }
        }
    }
}
