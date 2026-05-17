package com.example.twoscreen.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.twoscreen.data.model.Customer
import com.example.twoscreen.data.repository.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomerFormViewModel @Inject constructor(
    private val repository: CustomerRepository
) : ViewModel() {

    private var name = ""
    private var email = ""
    private var phone = ""
    private var city = ""

    private val _nameError = MutableLiveData<String?>()
    val nameError: LiveData<String?> = _nameError

    private val _emailError = MutableLiveData<String?>()
    val emailError: LiveData<String?> = _emailError

    private val _phoneError = MutableLiveData<String?>()
    val phoneError: LiveData<String?> = _phoneError

    private val _cityError = MutableLiveData<String?>()
    val cityError: LiveData<String?> = _cityError

    private val _isFormValid = MutableLiveData(false)
    val isFormValid: LiveData<Boolean> = _isFormValid

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _navigateToSuccess = MutableLiveData<Customer?>()
    val navigateToSuccess: LiveData<Customer?> = _navigateToSuccess

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun onNameChanged(value: String) {
        name = value
        _nameError.value = validateName(value)
        updateFormValidity()
    }

    fun onEmailChanged(value: String) {
        email = value
        _emailError.value = validateEmail(value)
        updateFormValidity()
    }

    fun onPhoneChanged(value: String) {
        phone = value
        _phoneError.value = validatePhone(value)
        updateFormValidity()
    }

    fun onCitySelected(value: String) {
        city = value
        _cityError.value = validateCity(value)
        updateFormValidity()
    }

    private fun validateName(value: String): String? {
        return when {
            value.isBlank() -> "Name is required"
            value.length < 2 -> "Name must be at least 2 characters"
            !value.matches(Regex("^[a-zA-Z ]+$")) -> "Name must not contain numbers or special characters"
            else -> null
        }
    }

    private fun validateEmail(value: String): String? {
        return when {
            value.isBlank() -> "Email is required"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches() -> "Invalid email format"
            else -> null
        }
    }

    private fun validatePhone(value: String): String? {
        return when {
            value.isBlank() -> "Phone number is required"
            !value.matches(Regex("^[0-9]+$")) -> "Phone must contain digits only"
            value.length < 7 -> "Phone must be at least 7 digits"
            value.length > 15 -> "Phone must be at most 15 digits"
            else -> null
        }
    }

    private fun validateCity(value: String): String? {
        val validCities = listOf("Dubai", "Abu Dhabi", "Sharjah", "Riyadh")
        return when {
            value.isBlank() -> "Please select a city"
            value !in validCities -> "Please select a valid city"
            else -> null
        }
    }

    private fun updateFormValidity() {
        _isFormValid.value =
            validateName(name) == null &&
            validateEmail(email) == null &&
            validatePhone(phone) == null &&
            validateCity(city) == null
    }

    fun submitForm() {
        if (_isFormValid.value != true) return

        val customer = Customer(
            name = name,
            email = email,
            phone = phone,
            city = city
        )

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.submitCustomer(customer)
                if (response.isSuccessful) {
                    _navigateToSuccess.value = customer
                } else {
                    _errorMessage.value = "Submission failed. Please try again."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onNavigationDone() {
        _navigateToSuccess.value = null
    }

    fun onErrorShown() {
        _errorMessage.value = null
    }
}