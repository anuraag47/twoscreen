package com.example.twoscreen.data.remote

import com.example.twoscreen.data.model.Customer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("customers")
    suspend fun submitCustomer(@Body customer: Customer): Response<Customer>
}