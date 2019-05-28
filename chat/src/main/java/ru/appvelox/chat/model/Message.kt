package ru.appvelox.chat.model

import java.time.Instant
import java.util.*

interface Message {
    fun getId(): Long
    fun getText(): String
    fun getAuthor(): Author
    fun getDate(): Date
    fun getRepliedMessage(): Message?
}