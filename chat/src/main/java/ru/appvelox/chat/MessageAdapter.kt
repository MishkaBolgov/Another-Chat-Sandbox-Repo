package ru.appvelox.chat

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_incoming_message.view.*
import org.joda.time.DateTime
import org.joda.time.Days
import ru.appvelox.chat.model.Message
import java.util.LinkedList

class MessageAdapter : RecyclerView.Adapter<MessageViewHolder>() {
    var loadMoreListener: ChatView.LoadMoreListener? = null

    var currentUserId: Long? = null
    var messageBackgroundCornerRadius = 30f
    var incomingMessageBackgroundColor = Color.argb(20, 33, 150, 243)
    var outgoingMessageBackgroundColor = Color.argb(20, 76, 175, 80)

    val incomingMessageBackground = GradientDrawable().apply {
        setColor(outgoingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
    }

    val outgoingMessageBackground = GradientDrawable().apply {
        setColor(incomingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    var outgoingMessageLayout = R.layout.item_incoming_message
    var incomingMessageLayout = R.layout.item_outgoing_message

    private val messageList = mutableListOf<Message>()

    fun addNewMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.indexOf(message))
    }

    fun addOldMessage(messages: List<Message>) {
        messages.forEach {message ->
            messageList.add(0, message)
        }
        val first = messageList.indexOf(messages.first())
        val last = messageList.indexOf(messages.last())
        notifyItemRangeInserted(
            messageList.indexOf(messages.last()),
            messages.size
        )
    }

    companion object {
        var counter = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val messageLayout = when (viewType) {
            MessageType.INCOMING.type -> incomingMessageLayout
            MessageType.OUTGOING.type -> outgoingMessageLayout
            else -> 0 // todo: fix this bullshit
        }

        val view = LayoutInflater.from(parent.context).inflate(messageLayout, parent, false)

        ++counter

        when (viewType) {
            MessageType.INCOMING.type -> view.applyIncomingStyle()
            MessageType.OUTGOING.type -> view.applyOutgoingStyle()
        }

        val viewHolder = MessageViewHolder(view).apply {
            setIsRecyclable(false)
        }

        view.setOnClickListener {
            requestPreviousMessagesFromListener()
        }

        return viewHolder
    }

    override fun getItemCount() = messageList.size//if (currentUserId != null) messageList.size else 0

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]
        if (position == 0) {
            holder.bind(message)
            return
        }

        val previousMessage = messageList[position - 1]
        val messageDate = DateTime(message.getDate()).withTimeAtStartOfDay()
        val previousMessageDate = DateTime(previousMessage.getDate()).withTimeAtStartOfDay()
        val daysBetweenMessages = Days.daysBetween(messageDate, previousMessageDate).days
        val showMessageDate = daysBetweenMessages != 0

        holder.bind(message, showMessageDate)
    }

    fun getLastMessageIndex(): Int {
        return messageList.lastIndex
    }

    override fun getItemViewType(position: Int): Int {
        val messageAuthorId = messageList[position].getAuthor().getId()

        return if (messageAuthorId == currentUserId)
            MessageType.INCOMING.type
        else
            MessageType.OUTGOING.type
    }

    enum class MessageType(val type: Int) {
        INCOMING(0), OUTGOING(1)
    }

    private fun View.applyOutgoingStyle() {
        outgoingMessageBackground.constantState?.let {
            this.contentContainer.background = it.newDrawable().mutate()
        }
    }

    private fun View.applyIncomingStyle() {
        incomingMessageBackground.constantState?.let {
            this.contentContainer.background = it.newDrawable().mutate()
        }
    }

    fun requestPreviousMessagesFromListener() {
        loadMoreListener?.requestPreviousMessages(2, messageList.size) { loadedMessages ->
            addOldMessage(loadedMessages)
        }
    }
}