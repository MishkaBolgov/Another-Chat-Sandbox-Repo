package ru.appvelox.chat.model

interface Message {
    fun getId(): Long
    fun getText(): String
    fun getAuthor(): Author
}