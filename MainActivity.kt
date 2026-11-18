
package com.example.taskapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskApp()
        }
    }
}

@Composable
fun TaskApp() {
    var task by remember { mutableStateOf("") }
    var tasks by remember {
        mutableStateOf(listOf("Complete college assignment", "Learn Kotlin", "Practice Android Studio"))
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F6FA))
                .padding(20.dp)
        ) {
            Text(
                text = "My Tasks",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF20244A)
            )

            Text(
                text = "Manage your daily activities",
                color = Color.Gray,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(25.dp))

            OutlinedTextField(
                value = task,
                onValueChange = { task = it },
                label = { Text("Enter a new task") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    if (task.isNotBlank()) {
                        tasks = tasks + task
                        task = ""
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add Task")
            }

            Spacer(modifier = Modifier.height(25.dp))

            Text(
                text = "Your Tasks",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tasks) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = false,
                                onCheckedChange = {}
                            )
                            Text(
                                text = item,
                                modifier = Modifier.padding(start = 8.dp),
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TaskAppPreview() {
    TaskApp()
}
