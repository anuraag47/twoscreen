package com.example.twoscreen.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.twoscreen.databinding.FragmentCustomerFormBinding
import com.example.twoscreen.ui.CustomerFormViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class CustomerFormFragment : Fragment() {

    private var _binding: FragmentCustomerFormBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CustomerFormViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCustomerFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCityDropdown()
        setupTextWatchers()
        setupSubmitButton()
        observeViewModel()
    }

    private fun setupCityDropdown() {
        val cities = listOf("Dubai", "Abu Dhabi", "Sharjah", "Riyadh")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cities)
        binding.actvCity.setAdapter(adapter)
        binding.actvCity.setOnItemClickListener { _, _, _, _ ->
            viewModel.onCitySelected(binding.actvCity.text.toString())
        }
    }

    private fun setupTextWatchers() {
        binding.etFullName.doAfterTextChanged {
            viewModel.onNameChanged(it.toString())
        }
        binding.etEmail.doAfterTextChanged {
            viewModel.onEmailChanged(it.toString())
        }
        binding.etPhone.doAfterTextChanged {
            viewModel.onPhoneChanged(it.toString())
        }
    }

    private fun setupSubmitButton() {
        binding.btnSubmit.setOnClickListener {
            viewModel.submitForm()
        }
    }

    private fun observeViewModel() {
        viewModel.isFormValid.observe(viewLifecycleOwner) { isValid ->
            binding.btnSubmit.isEnabled = isValid
        }

        viewModel.nameError.observe(viewLifecycleOwner) { error ->
            binding.tilFullName.error = error
        }
        viewModel.emailError.observe(viewLifecycleOwner) { error ->
            binding.tilEmail.error = error
        }
        viewModel.phoneError.observe(viewLifecycleOwner) { error ->
            binding.tilPhone.error = error
        }
        viewModel.cityError.observe(viewLifecycleOwner) { error ->
            binding.tilCity.error = error
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSubmit.isEnabled = !isLoading
        }

        viewModel.navigateToSuccess.observe(viewLifecycleOwner) { customer ->
            customer?.let {
                val action = CustomerFormFragmentDirections.actionFormToSuccess(
                    name = it.name,
                    email = it.email,
                    phone = it.phone,
                    city = it.city
                )
                findNavController().navigate(action)
                viewModel.onNavigationDone()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}