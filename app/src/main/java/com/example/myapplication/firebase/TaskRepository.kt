package com.example.myapplication.firebase

import com.example.myapplication.model.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class TaskRepository {

    private val db = Firebase.firestore
    private val tasksCollection = db.collection("tasks")

    fun addTask(task: Task, onResult: (Boolean) -> Unit) {
        val docRef = tasksCollection.document()
        task.id = docRef.id
        docRef.set(task).addOnCompleteListener { onResult(it.isSuccessful) }
    }

    fun getTasks(onResult: (List<Task>) -> Unit) {
        tasksCollection.get().addOnSuccessListener {
            val tasks = it.documents.mapNotNull { doc -> doc.toObject(Task::class.java) }
            onResult(tasks)
        }
    }

    fun updateTask(task: Task, onResult: (Boolean) -> Unit) {
        tasksCollection.document(task.id).set(task).addOnCompleteListener {
            onResult(it.isSuccessful)
        }
    }

    fun deleteTask(taskId: String, onResult: (Boolean) -> Unit) {
        tasksCollection.document(taskId).delete().addOnCompleteListener {
            onResult(it.isSuccessful)
        }
    }
}
