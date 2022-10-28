package com.example.belajartodolist.api

data class TodoData (
    val title: String,
    val description: String? = null,
    val done_at: String? = null,
)