package com.example.studynotes.ui.main

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studynotes.data.Note
import com.example.studynotes.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    note: Note?,
    isCornellInitial: Boolean,
    onSave: (Note) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Note details states
    var title by remember { mutableStateOf(note?.title ?: "") }
    var category by remember { mutableStateOf(note?.category ?: "عام") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var cueColumn by remember { mutableStateOf(note?.cueColumn ?: "") }
    var summaryColumn by remember { mutableStateOf(note?.summaryColumn ?: "") }
    val isCornell = note?.isCornell ?: isCornellInitial
    var isPinned by remember { mutableStateOf(note?.isPinned ?: false) }
    var noteColor by remember { mutableStateOf(note?.color ?: 0xFF7C3AED.toInt()) }

    // Cornell tabs (0 = Notes, 1 = Cues/Questions, 2 = Summary)
    var selectedCornellTab by remember { mutableIntStateOf(0) }

    // Predefined colors for cards
    val colorsList = listOf(
        0xFF7C3AED.toInt(), // purple
        0xFF0D9488.toInt(), // aqua
        0xFF3B82F6.toInt(), // blue
        0xFF10B981.toInt(), // green
        0xFFEF4444.toInt(), // red
        0xFFF59E0B.toInt()  // orange
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isCornell) "درس كورنيل التلخيصي" else "ملاحظة جديدة", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "رجوع")
                    }
                },
                actions = {
                    // Pin Button
                    IconButton(onClick = { isPinned = !isPinned }) {
                        Icon(
                            imageVector = if (isPinned) Icons.Default.PushPin else Icons.Default.PushPin,
                            tint = if (isPinned) AccentAmber else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            contentDescription = "تثبيت"
                        )
                    }
                    // Save Button
                    IconButton(onClick = {
                        val savedNote = Note(
                            id = note?.id ?: 0,
                            title = title,
                            content = content,
                            category = category,
                            isCornell = isCornell,
                            cueColumn = cueColumn,
                            summaryColumn = summaryColumn,
                            isPinned = isPinned,
                            color = noteColor,
                            timestamp = System.currentTimeMillis()
                        )
                        onSave(savedNote)
                    }) {
                        Icon(imageVector = Icons.Default.Save, contentDescription = "حفظ", tint = PrimaryPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Title Input
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("عنوان الملاحظة / الدرس") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(noteColor)
                )
            )

            // Category/Subject Input
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("المادة الدراسية (مثال: رياضيات، علوم)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(noteColor)
                )
            )

            if (isCornell) {
                // Cornell Notes Tab Selector
                TabRow(
                    selectedTabIndex = selectedCornellTab,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp))
                ) {
                    Tab(
                        selected = selectedCornellTab == 0,
                        onClick = { selectedCornellTab = 0 },
                        text = { Text("1. الملاحظات", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedCornellTab == 1,
                        onClick = { selectedCornellTab = 1 },
                        text = { Text("2. الكلمات المفتاحية", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                    Tab(
                        selected = selectedCornellTab == 2,
                        onClick = { selectedCornellTab = 2 },
                        text = { Text("3. الملخص", fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                    )
                }

                // Cornell Input fields
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    when (selectedCornellTab) {
                        0 -> {
                            OutlinedTextField(
                                value = content,
                                onValueChange = { content = it },
                                placeholder = { Text("اكتب الملاحظات والتفاصيل والشروحات الهامة للدرس هنا...") },
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(noteColor)
                                )
                            )
                        }
                        1 -> {
                            OutlinedTextField(
                                value = cueColumn,
                                onValueChange = { cueColumn = it },
                                placeholder = { Text("سجل هنا الأسئلة الاستقصائية، النقاط الرئيسية، والرموز لتستدعي بها الدرس...") },
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(noteColor)
                                )
                            )
                        }
                        2 -> {
                            OutlinedTextField(
                                value = summaryColumn,
                                onValueChange = { summaryColumn = it },
                                placeholder = { Text("لخص الدرس كاملاً في جملتين أو ثلاث جمل عند الانتهاء من المذاكرة...") },
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(noteColor)
                                )
                            )
                        }
                    }
                }
            } else {
                // Regular Notes Input Field
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("اكتب شروحاتك وملاحظاتك هنا...") },
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(noteColor)
                    )
                )
            }

            // Note Color Selector Row
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("لون البطاقة:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    colorsList.forEach { col ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(col))
                                .clickable { noteColor = col }
                                .padding(4.dp)
                        ) {
                            if (noteColor == col) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
