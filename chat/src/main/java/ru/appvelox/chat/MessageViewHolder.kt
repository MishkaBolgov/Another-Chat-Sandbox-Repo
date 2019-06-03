package ru.appvelox.chat

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.avatar.view.*
import kotlinx.android.synthetic.main.date.view.*
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.left_swipe_action_icon.view.*
import kotlinx.android.synthetic.main.reply.view.*
import ru.appvelox.chat.model.Message
import java.text.SimpleDateFormat

class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        var counter = 0
    }

    init {
        ++counter
        view.imageViewLeftSwipeActionIcon?.let {
            it.imageAlpha = 0
        }
    }

    fun bind(message: Message, showMessageDate: Boolean = false) {
        itemView.authorName.text = message.getAuthor().getName()
        itemView.message.text = message.getText()
        itemView.time.text = SimpleDateFormat("HH:mm").format(message.getDate())
        itemView.date.text = SimpleDateFormat("dd MMM").format(message.getDate())

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

        if(message.getImageUrl() == null){
            itemView.authorName.visibility = View.VISIBLE
            itemView.message.visibility = View.VISIBLE
            itemView.time.visibility = View.VISIBLE
            itemView.date.visibility = View.VISIBLE

            itemView.image.visibility = View.GONE
        } else {
            itemView.authorName.visibility = View.GONE
            itemView.message.visibility = View.GONE
            itemView.date.visibility = View.GONE
            itemView.replyContainer.visibility = View.GONE

            itemView.image.visibility = View.VISIBLE
            Picasso.get()
                .load(message.getImageUrl())
//                .transform(RoundedImage(20f))
//                .fit()
//                .centerInside()
                .into(itemView.image)

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