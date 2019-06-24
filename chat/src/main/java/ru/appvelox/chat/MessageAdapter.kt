package ru.appvelox.chat

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.joda.time.DateTime
import org.joda.time.Days
import ru.appvelox.chat.model.Message
import ru.appvelox.chat.model.TextMessage

open class MessageAdapter(val appearance: ChatAppearance, initTextMessages: List<TextMessage>? = null) :
    RecyclerView.Adapter<MessageViewHolder>() {

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
        if (initTextMessages != null)
            addAll(initTextMessages)
    }

    internal val selectedMessageList = mutableListOf<Message>()

    fun copyPropertiesTo(adapter: MessageAdapter) {
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

        val layout = when (viewType) {
            MessageType.INCOMING.type -> appearance.incomingMessageLayout
            MessageType.OUTGOING.type -> appearance.outgoingMessageLayout
            MessageType.INCOMING_IMAGE.type -> appearance.incomingImageLayout
            MessageType.OUTGOING_IMAGE.type -> appearance.outgoingImageLayout
            else -> appearance.outgoingMessageLayout
        }

        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)

        val viewHolder = when (viewType) {
            MessageType.INCOMING.type, MessageType.OUTGOING.type -> TextMessageViewHolder(
                view,
                appearance.getDateFormatter()
            )
            MessageType.INCOMING_IMAGE.type, MessageType.OUTGOING_IMAGE.type -> ImageViewHolder(
                view,
                appearance.getDateFormatter(),
                appearance.messageBackgroundCornerRadius,
                appearance.minImageMessageWidth,
                appearance.minImageMessageHeight,
                appearance.maxImageMessageWidth,
                appearance.maxImageMessageHeight
            )
            else -> TextMessageViewHolder(view, appearance.getDateFormatter())
        }

        return viewHolder
    }

    override fun getItemCount() = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        val message = messageList[position]

        val view = holder.itemView

//        if (onItemClickListener == null) {
//            view.findViewById<ViewGroup>(R.id.messageContainer).setOnClickListener(null)
//        } else {
//            view.findViewById<ViewGroup>(R.id.messageContainer).setOnClickListener {
//                onItemClickListener?.onClick(textMessage)
//            }
//        }
//
//        if (onItemLongClickListener == null) {
//            view.findViewById<ViewGroup>(R.id.messageContainer).setOnLongClickListener(null)
//        } else {
//            view.findViewById<ViewGroup>(R.id.messageContainer).setOnLongClickListener {
//                onItemLongClickListener?.onLongClick(textMessage)
//                true
//            }
//        }
//
//        view.findViewById<ViewGroup>(R.id.replyContainer).setOnClickListener {
//            textMessage.getRepliedMessage()?.let {
//                onReplyClickListener?.onReplyClick(it)
//            }
//        }
//
//        onItemLongClickListener?.let { listener ->
//            view.findViewById<ViewGroup>(R.id.messageContainer).setOnLongClickListener {
//                listener.onLongClick(textMessage)
//                true
//            }
//        }

        if (position == 0) {
            holder.bind(message, true, getItemViewType(position).toMessageType())
            return
        }

        val previousMessage = messageList[position - 1]
        val messageDate = DateTime(message.getDate()).withTimeAtStartOfDay()
        val previousMessageDate = DateTime(previousMessage.getDate()).withTimeAtStartOfDay()
        val daysBetweenMessages = Days.daysBetween(messageDate, previousMessageDate).days
        val showMessageDate = daysBetweenMessages != 0

        holder.bind(message, showMessageDate, getItemViewType(position).toMessageType())

    }

    fun getLastMessageIndex(): Int {
        return messageList.lastIndex
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.isIncoming()) {
            if (message is TextMessage)
                MessageType.INCOMING.type
            else
                MessageType.INCOMING_IMAGE.type
        } else {
            if (message is TextMessage)
                MessageType.OUTGOING.type
            else
                MessageType.OUTGOING_IMAGE.type
        }
    }

    protected fun Message.isIncoming(): Boolean {
        val messageAuthorId = getAuthor().getId()
        return messageAuthorId != currentUserId
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

    fun deleteMessage(textMessage: TextMessage) {
        val position = messageList.indexOf(textMessage)
        messageList.remove(textMessage)
        notifyItemRemoved(position)
    }

    fun updateMessage(textMessage: TextMessage) {
        val index = messageList.indexOf(messageList.find { it.getId() == textMessage.getId() })
        messageList[index] = textMessage
        notifyItemChanged(index)
    }

    fun addMessages(textMessages: MutableList<TextMessage>) {
        messageList.addAll(textMessages)
        notifyDataSetChanged()
    }
}