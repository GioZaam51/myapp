package com.example.myapplication.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.firebase.TaskRepository
import com.example.myapplication.model.Task

class TaskViewModel : ViewModel() {
    private val repository = TaskRepository()

    val tasks = MutableLiveData<List<Task>>()

    fun loadTasks() {
        repository.getTasks {
            tasks.value = it
        }
    }

    fun addTask(task: Task, onResult: (Boolean) -> Unit) {
        repository.addTask(task, onResult)
    }

    fun updateTask(task: Task, onResult: (Boolean) -> Unit) {
        repository.updateTask(task, onResult)
    }

    fun deleteTask(taskId: String, onResult: (Boolean) -> Unit) {
        repository.deleteTask(taskId, onResult)
    }
}
