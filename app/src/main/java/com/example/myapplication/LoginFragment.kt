package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.viewModels
import com.example.myapplication.viewmodel.LoginViewModel
import android.widget.Toast
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.FragmentLoginBinding
import com.example.myapplication.interfaces.FragmentCommunicator

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: LoginViewModel by viewModels()

    private var communicator: FragmentCommunicator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        communicator = activity as? FragmentCommunicator
        setupView()
        observeViewModel()
        return binding.root
    }

    private fun setupView() {
        binding.tvRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.btnIngresar.setOnClickListener {
            val correo = binding.etCorreo.text.toString()
            val pass = binding.etContrasena.text.toString()
            viewModel.login(correo, pass)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnIngresar.isEnabled = !isLoading
            if (isLoading) {
                communicator?.showLoader()
                binding.loginContainer.visibility = View.GONE  // Oculta formulario
            } else {
                communicator?.hideLoader()
                binding.loginContainer.visibility = View.VISIBLE // Muestra formulario
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMsg ->
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_SHORT).show()
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_taskListFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
