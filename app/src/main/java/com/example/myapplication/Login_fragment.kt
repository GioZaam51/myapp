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
import com.example.myapplication.databinding.FragmentLoginFragmentBinding

class Login_fragment : Fragment() {

    private var _binding: FragmentLoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginFragmentBinding.inflate(inflater, container, false)
        setupView()
        observeViewModel()
        return binding.root
    }

    private fun setupView() {
        binding.tvRegistrar.setOnClickListener {
            findNavController().navigate(R.id.action_login_fragment_to_register_fragment)
        }

        binding.btnIngresar.setOnClickListener {
            val correo = binding.etCorreo.text.toString()
            val pass = binding.etContrasena.text.toString()
            viewModel.login(correo, pass)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.btnIngresar.isEnabled = !it
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.loginSuccess.observe(viewLifecycleOwner) {
            if (it) {
                findNavController().navigate(R.id.action_login_fragment_to_taskListFragment)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
