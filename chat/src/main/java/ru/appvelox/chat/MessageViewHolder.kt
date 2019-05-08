package ru.appvelox.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import ru.appvelox.chat.model.Message

class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    fun bind(message: Message) {
        itemView.authorName.text = message.getAuthor().getName()
        itemView.messageText.text = "${message.getText()} #${message.getId()}"
    }
}
