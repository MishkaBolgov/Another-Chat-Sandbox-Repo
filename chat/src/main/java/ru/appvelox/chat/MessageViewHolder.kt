package ru.appvelox.chat

import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation
import kotlinx.android.synthetic.main.avatar.view.*
import kotlinx.android.synthetic.main.date.view.*
import kotlinx.android.synthetic.main.item_outgoing_message.view.*
import kotlinx.android.synthetic.main.item_incoming_message.view.*
import kotlinx.android.synthetic.main.item_incoming_message.view.authorName
import kotlinx.android.synthetic.main.item_incoming_message.view.messageText
import kotlinx.android.synthetic.main.item_incoming_message.view.time
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
        itemView.authorName.text = message.getAuthor().getName() + " #${message.getId()}"
        itemView.messageText.text = message.getText()
        itemView.time.text = SimpleDateFormat("HH:mm").format(message.getDate())
        itemView.date.text = SimpleDateFormat("dd MMM").format(message.getDate())

        itemView.avatar?.let {
            Picasso.get()
                .load(message.getAuthor().getAvatar())
                .transform(CircularAvatar())
                .into(it)
        }

        if(showMessageDate)
            itemView.dateContainer.visibility = View.VISIBLE
    }
}