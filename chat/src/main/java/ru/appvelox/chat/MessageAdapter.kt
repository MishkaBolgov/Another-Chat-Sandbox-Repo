package ru.appvelox.chat

import android.os.Handler
import android.os.Looper
import android.provider.Telephony
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import org.joda.time.DateTime
import org.joda.time.Days
import ru.appvelox.chat.model.Message

open class MessageAdapter(val appearance: ChatAppearance, initMessages: List<Message>? = null) : RecyclerView.Adapter<MessageViewHolder>() {

    var onReplyClickListener: ChatView.OnReplyClickListener? = null

    var loadMoreListener: ChatView.LoadMoreListener? = null

    var currentUserId: Long? = null

    var oldDataLoading = false

    var onItemClickListener: ChatView.OnMessageClickListener? = null
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    var onItemLongClickListener: ChatView.OnMessageLongClickListener? = null
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    internal val messageList = mutableListOf<Message>().apply {
        if(initMessages!=null)
            addAll(initMessages)
    }

    internal val selectedMessageList = mutableListOf<Message>()

    fun copyPropertiesTo(adapter: MessageAdapter){
        adapter.onReplyClickListener = onReplyClickListener
        adapter.loadMoreListener = loadMoreListener
        adapter.currentUserId = currentUserId
        adapter.oldDataLoading = oldDataLoading
        adapter.onItemClickListener = onItemClickListener
        adapter.onItemLongClickListener = onItemLongClickListener

        adapter.messageList.clear()
        adapter.messageList.addAll(messageList)

        adapter.selectedMessageList.clear()
        adapter.selectedMessageList.addAll(selectedMessageList)
    }

    fun addNewMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.indexOf(message))
    }

    fun addOldMessages(messages: List<Message>) {
        messages.forEach { message ->
            messageList.add(0, message)
        }

        notifyMessagesInserted(messages)
    }

    fun notifyMessagesInserted(messages: List<Message>) {
        Handler(Looper.getMainLooper()).post {
            notifyItemRangeInserted(
                messageList.indexOf(messages.last()),
                messages.size
            )
            oldDataLoading = false
        }
    }

    fun changeMessageSelection(message: Message) {
        if (selectedMessageList.contains(message))
            selectedMessageList.remove(message)
        else
            selectedMessageList.add(message)

        notifyItemChanged(messageList.indexOf(message), null)
    }


    fun eraseSelectedMessages() {
        val messages = mutableListOf<Message>()

        selectedMessageList.forEach { messages.add(it) }

        selectedMessageList.clear()

        messages.forEach { notifyItemChanged(messageList.indexOf(it)) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        var layout = when (viewType) {
            MessageType.INCOMING.type -> appearance.incomingMessageLayout
            MessageType.OUTGOING.type -> appearance.outgoingMessageLayout
            else -> appearance.outgoingMessageLayout
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        val viewHolder = MessageViewHolder(view)

        return viewHolder
    }

    override fun getItemCount() = messageList.size//if (currentUserId != null) messageList.size else 0

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val message = messageList[position]

        val view = holder.itemView

        if (onItemClickListener == null) {
            view.findViewById<ViewGroup>(R.id.messageContainer).setOnClickListener(null)
        } else {
            view.findViewById<ViewGroup>(R.id.messageContainer).setOnClickListener {
                onItemClickListener?.onClick(message)
            }
        }

        if (onItemLongClickListener == null) {
            view.findViewById<ViewGroup>(R.id.messageContainer).setOnLongClickListener(null)
        } else {
            view.findViewById<ViewGroup>(R.id.messageContainer).setOnLongClickListener {
                onItemLongClickListener?.onLongClick(message)
                true
            }
        }

        view.findViewById<ViewGroup>(R.id.replyContainer).setOnClickListener {
            message.getRepliedMessage()?.let {
                onReplyClickListener?.onReplyClick(it)
            }
        }

        onItemLongClickListener?.let { listener ->
            view.findViewById<ViewGroup>(R.id.messageContainer).setOnLongClickListener {
                listener.onLongClick(message)
                true
            }
        }

        if (position == 0) {
            holder.bind(message, true, appearance.getDateFormatter())
            return
        }

        val previousMessage = messageList[position - 1]
        val messageDate = DateTime(message.getDate()).withTimeAtStartOfDay()
        val previousMessageDate = DateTime(previousMessage.getDate()).withTimeAtStartOfDay()
        val daysBetweenMessages = Days.daysBetween(messageDate, previousMessageDate).days
        val showMessageDate = daysBetweenMessages != 0

        holder.bind(message, showMessageDate, appearance.getDateFormatter())


    }

    fun getLastMessageIndex(): Int {
        return messageList.lastIndex
    }

    override fun getItemViewType(position: Int) =
        if (messageList[position].isIncoming())
            MessageType.INCOMING.type
        else
            MessageType.OUTGOING.type

    protected fun Message.isIncoming(): Boolean {
        val messageAuthorId = getAuthor().getId()
        return messageAuthorId != currentUserId
    }

    enum class MessageType(val type: Int) {
        INCOMING(0), OUTGOING(1)
    }

    fun requestPreviousMessagesFromListener() {
        loadMoreListener?.requestPreviousMessages(20, messageList.size, object : ChatView.LoadMoreCallback {
            override fun onResult(messages: List<Message>) {
                oldDataLoading = true
                addOldMessages(messages)
            }
        })
    }

    fun notifyAppearanceChanged() {
        notifyDataSetChanged()
    }

    fun getPositionOfMessage(message: Message): Int {
        return messageList.indexOf(message)
    }

    fun deleteMessage(message: Message) {
        val position = messageList.indexOf(message)
        messageList.remove(message)
        notifyItemRemoved(position)
    }

    fun updateMessage(message: Message) {
        val index = messageList.indexOf(messageList.find { it.getId() == message.getId() })
        messageList[index] = message
        notifyItemChanged(index)
    }

    fun addMessages(messages: MutableList<Message>) {
        messageList.addAll(messages)
        notifyDataSetChanged()
    }
}