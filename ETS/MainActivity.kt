package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskManagerApp()
                }
            }
        }
    }
}

// Model untuk tugas
data class Task(
    val id: UUID = UUID.randomUUID(),
    val title: String,
    val deadline: Date,
    var isCompleted: Boolean = false
)

// ViewModel untuk mengelola state aplikasi
class TaskViewModel : ViewModel() {
    private val _tasks = mutableStateOf<List<Task>>(listOf())
    val tasks: State<List<Task>> = _tasks

    private val _sortOrder = mutableStateOf(SortOrder.BY_DEADLINE)
    val sortOrder: State<SortOrder> = _sortOrder

    fun addTask(title: String, deadline: Date) {
        if (title.isNotBlank()) {
            val newTask = Task(title = title, deadline = deadline)
            _tasks.value = _tasks.value + newTask
            sortTasks()
        }
    }

    fun removeTask(task: Task) {
        _tasks.value = _tasks.value.filter { it.id != task.id }
    }

    fun toggleTaskStatus(task: Task) {
        _tasks.value = _tasks.value.map {
            if (it.id == task.id) it.copy(isCompleted = !it.isCompleted) else it
        }
        sortTasks()
    }

    fun changeSortOrder(order: SortOrder) {
        _sortOrder.value = order
        sortTasks()
    }

    private fun sortTasks() {
        _tasks.value = when (_sortOrder.value) {
            SortOrder.BY_DEADLINE -> _tasks.value.sortedBy { it.deadline }
            SortOrder.BY_STATUS -> _tasks.value.sortedBy { it.isCompleted }
        }
    }
}

enum class SortOrder {
    BY_DEADLINE, BY_STATUS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskManagerApp(viewModel: TaskViewModel = viewModel()) {
    val tasks by viewModel.tasks
    val sortOrder by viewModel.sortOrder
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            // Gunakan CenterAlignedTopAppBar yang lebih stabil
            CenterAlignedTopAppBar(
                title = { Text("Daily Task Manager") },
                // Hindari topAppBarColors untuk saat ini
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Sorting controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sort by:",
                    style = MaterialTheme.typography.titleMedium
                )
                Row {
                    FilterChip(
                        selected = sortOrder == SortOrder.BY_DEADLINE,
                        onClick = { viewModel.changeSortOrder(SortOrder.BY_DEADLINE) },
                        label = { Text("Deadline") },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    FilterChip(
                        selected = sortOrder == SortOrder.BY_STATUS,
                        onClick = { viewModel.changeSortOrder(SortOrder.BY_STATUS) },
                        label = { Text("Status") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Task list
            if (tasks.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No tasks yet. Add some tasks!",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(
                            task = task,
                            onToggleStatus = { viewModel.toggleTaskStatus(task) },
                            onRemove = { viewModel.removeTask(task) }
                        )
                    }
                }
            }
        }

        if (showDialog) {
            AddTaskDialog(
                onDismiss = { showDialog = false },
                onTaskAdded = { title, deadline ->
                    viewModel.addTask(title, deadline)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun TaskItem(
    task: Task,
    onToggleStatus: () -> Unit,
    onRemove: () -> Unit
) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (task.isCompleted)
                MaterialTheme.colorScheme.surfaceVariant
            else
                MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isCompleted,
                onCheckedChange = { onToggleStatus() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null
                )
                Text(
                    text = "Due: ${dateFormat.format(task.deadline)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            IconButton(onClick = onRemove) {
                Text("×", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (String, Date) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var dateText by remember { mutableStateOf("") }
    var timeText by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var titleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        titleError = it.isBlank()
                    },
                    label = { Text("Task Title") },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Title cannot be empty") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = dateText,
                    onValueChange = {
                        dateText = it
                        dateError = false
                    },
                    label = { Text("Date (dd/mm/yyyy)") },
                    isError = dateError,
                    supportingText = if (dateError) {
                        { Text("Enter valid date format") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = timeText,
                    onValueChange = {
                        timeText = it
                        timeError = false
                    },
                    label = { Text("Time (HH:MM)") },
                    isError = timeError,
                    supportingText = if (timeError) {
                        { Text("Enter valid time format") }
                    } else null,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    titleError = title.isBlank()
                    dateError = !isValidDate(dateText)
                    timeError = !isValidTime(timeText)

                    if (!titleError && !dateError && !timeError) {
                        val deadline = parseDateTime(dateText, timeText)
                        onTaskAdded(title, deadline)
                    }
                }
            ) {
                Text("Add Task")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// Utility functions for date and time validation
fun isValidDate(date: String): Boolean {
    val pattern = Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}\$")
    return pattern.matches(date)
}

fun isValidTime(time: String): Boolean {
    val pattern = Regex("^([01]?\\d|2[0-3]):([0-5]\\d)\$")
    return pattern.matches(time)
}

fun parseDateTime(dateStr: String, timeStr: String): Date {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return dateFormat.parse("$dateStr $timeStr") ?: Date()
}
