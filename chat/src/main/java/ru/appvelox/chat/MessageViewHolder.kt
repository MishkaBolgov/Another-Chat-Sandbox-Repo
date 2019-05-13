package ru.appvelox.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.date.view.*
import kotlinx.android.synthetic.main.item_outgoing_message.view.*
import ru.appvelox.chat.model.Message
import java.text.SimpleDateFormat

class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
    companion object{
        var counter = 0
    }

    init {
        ++counter
    }

    fun bind(message: Message, showMessageDate: Boolean = false) {
        itemView.authorName.text = message.getAuthor().getName()
        itemView.messageText.text = message.getText()
        itemView.time.text = SimpleDateFormat("HH:mm").format(message.getDate())
        itemView.date.text = SimpleDateFormat("dd MMM").format(message.getDate())

        itemView.avatar?.let {
            Picasso.get().load(message.getAuthor().getAvatar()).into(it)
        }

        if(showMessageDate)
            itemView.dateContainer.visibility = View.VISIBLE
    }
}