package com.voen.socketapp

data class ChatType(
    val type: String
)

data class ChatMessage(
    val type: String,
    val message: String
)

data class ChatUsers(
    val type: String,
    val message: List<String>
)