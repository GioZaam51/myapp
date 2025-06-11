package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.myapplication.databinding.FragmentTaskListBinding
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.adapter.TaskAdapter
import com.example.myapplication.model.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val taskList = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        setupRecyclerView()
        setupListeners()
        loadTasksFromFirebase()
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter(
            taskList,
            onEditClick = { task ->
                val bundle = Bundle().apply {
                    putString("id", task.id)
                    putString("name", task.name)
                    putString("description", task.description)
                    putString("date", task.date)
                }
                findNavController().navigate(R.id.action_taskListFragment_to_tareaFragment, bundle)

            },
            onDeleteClick = { task ->
                deleteTaskFromFirebase(task)
            }
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }


    private fun setupListeners() {
        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_tareaFragment)
        }

        binding.menuIcon.setOnClickListener {
            (activity as? MainActivity)?.toggleDrawer()
        }
        binding.addFab.setOnClickListener {
            findNavController().navigate(R.id.action_taskListFragment_to_tareaFragment)
        }
    }

    private fun loadTasksFromFirebase() {
        val db = FirebaseDatabase.getInstance().getReference("tasks")
        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                for (data in snapshot.children) {
                    val task = data.getValue(Task::class.java)
                    task?.let { taskList.add(it.copy(id = data.key ?: "")) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteTaskFromFirebase(task: Task) {
        val db = FirebaseDatabase.getInstance().getReference("tasks")
        db.child(task.id).removeValue().addOnSuccessListener {
            Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
        }
    }

}
