package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.myapplication.model.Task
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.database.FirebaseDatabase

class TareaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.fragment_tarea, container, false)

        val args = arguments
        val id = args?.getString("id") ?: ""
        val name = args?.getString("name") ?: ""
        val description = args?.getString("description") ?: ""
        val date = args?.getString("date") ?: ""

        val nameEditText = view.findViewById<EditText>(R.id.etTaskName)
        val descriptionEditText = view.findViewById<EditText>(R.id.etTaskDescription)
        val dateEditText = view.findViewById<EditText>(R.id.etTaskDate)
        val loader = view.findViewById<LottieAnimationView>(R.id.loaderAnimation)

        nameEditText.setText(name)
        descriptionEditText.setText(description)
        dateEditText.setText(date)

        val backIcon = view.findViewById<View>(R.id.backIcon)
        backIcon.setOnClickListener {
            findNavController().navigate(R.id.action_tareaFragment_to_taskListFragment)
        }

        val addButton = view.findViewById<Button>(R.id.btnAddTask)

        addButton.setOnClickListener {
            val taskName = nameEditText.text.toString().trim()
            val taskDesc = descriptionEditText.text.toString().trim()
            val taskDate = dateEditText.text.toString().trim()

            if (taskName.isEmpty() || taskDesc.isEmpty() || taskDate.isEmpty()) {
                Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Mostrar el loader
            showLoader(loader, true)
            addButton.isEnabled = false

            val dbRef = FirebaseDatabase.getInstance().getReference("tasks")

            if (id.isEmpty()) {
                // Crear nueva tarea
                val taskId = dbRef.push().key!!
                val newTask = Task(taskId, taskName, taskDesc, taskDate)

                dbRef.child(taskId).setValue(newTask).addOnSuccessListener {
                    showLoader(loader, false)
                    addButton.isEnabled = true
                    Toast.makeText(requireContext(), "Tarea agregada", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_tareaFragment_to_taskListFragment)
                }.addOnFailureListener {
                    showLoader(loader, false)
                    addButton.isEnabled = true
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Actualizar tarea existente
                val updatedTask = Task(id, taskName, taskDesc, taskDate)

                dbRef.child(id).setValue(updatedTask).addOnSuccessListener {
                    showLoader(loader, false)
                    addButton.isEnabled = true
                    Toast.makeText(requireContext(), "Tarea actualizada", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_tareaFragment_to_taskListFragment)
                }.addOnFailureListener {
                    showLoader(loader, false)
                    addButton.isEnabled = true
                    Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view
    }

    private fun showLoader(loader: LottieAnimationView, show: Boolean) {
        loader.visibility = if (show) View.VISIBLE else View.GONE
        if (show) loader.playAnimation() else loader.pauseAnimation()
    }
}
