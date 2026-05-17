package com.example.twoscreen.data.model

import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("name")  val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("city")  val city: String
)