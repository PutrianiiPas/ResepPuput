package com.example.etsresepapp

data class Recipe(
    val id: Int,
    val name: String,
    val image: Int,
    val ingredients: List<String>,
    val seasonings: List<String>,
    val steps: List<String>,
    val description: String
)