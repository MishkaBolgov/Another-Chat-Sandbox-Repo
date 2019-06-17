package ru.appvelox.chat

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.avatar.view.*
import kotlinx.android.synthetic.main.date.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.left_swipe_action_icon.view.*
import ru.appvelox.chat.model.Message
import java.text.SimpleDateFormat

open class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var message: Message? = null


    companion object {
        var counter = 0
    }
    init {
        view.imageViewLeftSwipeActionIcon?.imageAlpha = 0
    }

    open fun bind(message: Message, showMessageDate: Boolean = false, dateFormatter: ChatView.DateFormatter, messageType: MessageType, radius: Float) {
        this.message = message
        itemView.authorName.text = message.getAuthor().getName()
        itemView.message.text = message.getText()
        itemView.time.text = dateFormatter.formatTime(message.getDate())
        itemView.date.text = dateFormatter.formatDate(message.getDate())

        itemView.avatar?.let {
            Picasso.get()
                .load(message.getAuthor().getAvatar())
                .transform(CircularAvatar())
                .into(it)
        }

        if (showMessageDate)
            itemView.dateContainer.visibility = View.VISIBLE
        else
            itemView.dateContainer.visibility = View.GONE

        val replyMessage = message.getRepliedMessage()
        if(replyMessage == null){
            if (itemView.replyContainer != null)
                itemView.replyContainer.visibility = View.GONE
        } else {
            if (itemView.replyContainer != null) {
                itemView.replyContainer.visibility = View.VISIBLE
                itemView.replyAuthorName.text = replyMessage.getAuthor().getName()
                itemView.replyMessage.text = replyMessage.getText()
            }
        }


//        val isRead = message.isRead()
//
//        if (isRead == null || isRead == false){
//            itemView.isRead.visibility = View.GONE
//        } else {
//            itemView.isRead.visibility = View.VISIBLE
//        }

//        val isSent = message.isSent()
//
//        if (isSent == null || isSent == false){
//            itemView.isSent.visibility = View.GONE
//        } else {
//            itemView.isSent.visibility = View.VISIBLE
//        }

    }
}