package ru.appvelox.chat.model

import java.util.*

interface Message {
    fun getId(): Long
    fun getText(): String
    fun getAuthor(): Author
    fun getDate(): Date
    fun isSent(): Boolean?
    fun isRead(): Boolean?
}