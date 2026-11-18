package com.example.taskapp
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.*
import kotlinx.coroutines.launch

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String
)

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: TaskEntity)
}

@Database(entities = [TaskEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

class TaskViewModel(private val dao: TaskDao) : ViewModel() {
    var message by mutableStateOf("")
        private set

    fun saveTask(title: String) {
        viewModelScope.launch {
            dao.insert(TaskEntity(title = title))
            message = "Task Saved Successfully!"
        }
    }
}

class TaskFactory(private val dao: TaskDao) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TaskViewModel(dao) as T
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(
            applicationContext,
            TaskDatabase::class.java,
            "task_db"
        ).build()

        setContent {
            val vm: TaskViewModel = viewModel(
                factory = TaskFactory(db.taskDao())
            )
            TaskRoute(vm)
        }
    }
}

@Composable
fun TaskRoute(vm: TaskViewModel) {
    var title by remember { mutableStateOf("") }

    TaskScreen(
        title = title,
        message = vm.message,
        onTitleChange = { title = it },
        onSave = {
            if (title.isNotBlank()) {
                vm.saveTask(title)
                title = ""
            }
        }
    )
}

@Composable
fun TaskScreen(
    title: String,
    message: String,
    onTitleChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Task Entry Form", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Enter Task") }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = onSave, enabled = title.isNotBlank()) {
            Text("Save Task")
        }

        Text(message)
    }
}
