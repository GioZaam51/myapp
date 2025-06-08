package com.example.myapplication

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentRegisterFragmentBinding
import com.example.myapplication.interfaces.FragmentCommunicator
import com.example.myapplication.viewmodel.RegisterViewModel

class Register_fragment : Fragment() {

    private var _binding: FragmentRegisterFragmentBinding? = null
    private val binding get() = _binding!!

    private val registerViewModel: RegisterViewModel by viewModels()

    private var fragmentCommunicator: FragmentCommunicator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is FragmentCommunicator) {
            fragmentCommunicator = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        fragmentCommunicator = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterFragmentBinding.inflate(inflater, container, false)
        setupView()
        observeViewModel()
        return binding.root
    }

    private fun setupView() {
        binding.btnIcono.setOnClickListener {
            findNavController().navigate(R.id.action_register_fragment_to_login_fragment)
        }

        binding.btnIngresar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val correo = binding.etCorreo.text.toString().trim()
            val contrasena = binding.etContrasena.text.toString().trim()

            if (nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()) {
                Toast.makeText(requireContext(), "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            } else {
                registerViewModel.registerUser(correo, contrasena)
            }
        }
    }

    private fun observeViewModel() {
        registerViewModel.showLoader.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                fragmentCommunicator?.showLoader()
            } else {
                fragmentCommunicator?.hideLoader()
            }
            binding.btnIngresar.isEnabled = !isLoading
        })

        registerViewModel.registrationSuccess.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registro exitoso", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_register_fragment_to_login_fragment)
            }
        })

        registerViewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
