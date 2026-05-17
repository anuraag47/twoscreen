package com.example.twoscreen.data.repository

import com.example.twoscreen.data.model.Customer
import com.example.twoscreen.data.remote.ApiService
import retrofit2.Response
import javax.inject.Inject

class CustomerRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun submitCustomer(customer: Customer): Response<Customer> {
        return apiService.submitCustomer(customer)
    }
}