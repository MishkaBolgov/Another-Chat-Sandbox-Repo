package ru.appvelox.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import ru.appvelox.chat.model.Message
import ru.appvelox.chat.model.TextMessage

abstract class MessageViewHolder(view: View, protected val dateFormatter: ChatView.DateFormatter) :
    RecyclerView.ViewHolder(view) {
    var message: Message? = null
    open fun bind(
        message: Message,
        showMessageDate: Boolean,
        messageType: MessageType? = null
    ){
        this.message = message
    }
}