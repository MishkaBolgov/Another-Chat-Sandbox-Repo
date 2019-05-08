package ru.appvelox.chat

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import ru.appvelox.chat.model.Message

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    var currentUserId: Long? = null
    var messageBackgroundCornerRadius = 20f
    var incomingMessageBackgroundColor = Color.argb(20, 33, 150, 243)
    var outcomingMessageBackgroundColor = Color.argb(20, 76, 175, 80)

    var incomingMessageBackground = GradientDrawable().apply {
        setColor(incomingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    var outcomingMessageBackground = GradientDrawable().apply {
        setColor(outcomingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf( radius, radius, 0f, 0f, radius, radius, radius, radius)
    }


    private val messageList = mutableListOf<Message>()

    fun addMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.indexOf(message))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        when (viewType) {
            MessageType.INCOMING.type -> view.layoutMessage.setIncomingView()
            MessageType.OUTCOMING.type -> view.layoutMessage.setOutcomingView()
        }

        return MessageViewHolder(view)
    }

    override fun getItemCount() = if (currentUserId != null) messageList.size else 0

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messageList[position])
    }

    fun getLastMessageIndex(): Int {
        return messageList.lastIndex
    }

    override fun getItemViewType(position: Int): Int {
        super.getItemViewType(position)
        val messageAuthorId = messageList[position].getAuthor().getId()
        return if (messageList[position].getAuthor().getId() == currentUserId)
            MessageType.OUTCOMING.type
        else
            MessageType.INCOMING.type
    }

    enum class MessageType(val type: Int) {
        INCOMING(0), OUTCOMING(1)
    }

    private fun View.setIncomingView() {
        background = incomingMessageBackground
        val newParams = layoutParams as ConstraintLayout.LayoutParams

        val constraintSet = ConstraintSet()
        constraintSet.clone(this as ConstraintLayout)

    }

    private fun View.setOutcomingView() {
        background = outcomingMessageBackground
    }

}


