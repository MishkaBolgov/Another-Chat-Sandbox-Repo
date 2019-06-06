package ru.appvelox.chat

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*
import kotlinx.android.synthetic.main.left_swipe_action_icon.view.*
import kotlinx.android.synthetic.main.reply.view.*
import org.joda.time.DateTime
import org.joda.time.Days
import ru.appvelox.chat.model.Message

internal class MessageAdapter(val appearance: IAppearance) : RecyclerView.Adapter<MessageViewHolder>() {

    var onReplyClickListener: ChatView.OnReplyClickListener? = null

    var loadMoreListener: ChatView.LoadMoreListener? = null

    var currentUserId: Long? = null

    var oldDataLoading = false

    var onItemClickListener: ChatView.OnItemClickListener? = null
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    var onItemLongClickListener: ChatView.OnItemLongClickListener? = null
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    var customMessageLayout: Int? = null

    internal val messageList = mutableListOf<Message>()
    internal val selectedMessageList = mutableListOf<Message>()

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

        val view = if (customMessageLayout == null)
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        else
            LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)

        when (viewType) {
            MessageType.INCOMING.type -> view.applyIncomingAppearance()
            MessageType.OUTGOING.type -> view.applyOutgoingAppearance()
        }

        val viewHolder = MessageViewHolder(view)

        return viewHolder
    }

    override fun getItemCount() = messageList.size//if (currentUserId != null) messageList.size else 0

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messageList[position]

        if (onItemClickListener == null) {
            holder.itemView.messageContainer.setOnClickListener(null)
        } else {
            holder.itemView.messageContainer.setOnClickListener {
                onItemClickListener?.onClick(message)
            }
        }

        if (onItemLongClickListener == null) {
            holder.itemView.messageContainer.setOnLongClickListener(null)
        } else {
            holder.itemView.messageContainer.setOnLongClickListener {
                onItemLongClickListener?.onLongClick(message)
                true
            }
        }

        holder.itemView.replyContainer.setOnClickListener {
            message.getRepliedMessage()?.let {
                onReplyClickListener?.onReplyClick(it)
            }
        }

        onItemLongClickListener?.let { listener ->
            holder.itemView.messageContainer.setOnLongClickListener {
                listener.onLongClick(message)
                true
            }
        }

        if (position == 0) {
            holder.bind(message, true)
            return
        }

        val previousMessage = messageList[position - 1]
        val messageDate = DateTime(message.getDate()).withTimeAtStartOfDay()
        val previousMessageDate = DateTime(previousMessage.getDate()).withTimeAtStartOfDay()
        val daysBetweenMessages = Days.daysBetween(messageDate, previousMessageDate).days
        val showMessageDate = daysBetweenMessages != 0

        holder.bind(message, showMessageDate)


        if (selectedMessageList.contains(message))
            if (message.isIncoming())
                holder.itemView.applyIncomingSelectedAppearance()
            else
                holder.itemView.applyOutgoingSelectedAppearance()
        else
            if (message.isIncoming())
                holder.itemView.applyIncomingAppearance()
            else
                holder.itemView.applyOutgoingAppearance()


    }

    fun getLastMessageIndex(): Int {
        return messageList.lastIndex
    }

    override fun getItemViewType(position: Int) =
        if (messageList[position].isIncoming())
            MessageType.INCOMING.type
        else
            MessageType.OUTGOING.type

    private fun Message.isIncoming(): Boolean {
        val messageAuthorId = getAuthor().getId()
        return messageAuthorId != currentUserId
    }

    enum class MessageType(val type: Int) {
        INCOMING(0), OUTGOING(1)
    }

    private fun View.applyOutgoingAppearance() {
        applyCommonStyle()
        applyOutgoingConstraints()
        isRead.visibility = View.VISIBLE
        this.messageContainer.background = appearance.getOutgoingMessageBackground()
    }

    private fun View.applyIncomingAppearance() {
        applyCommonStyle()
        applyIncomingConstraints()
        isRead.visibility = View.GONE
        this.messageContainer.background = appearance.getIncomingMessageBackground()
    }

    private fun View.applyCommonStyle() {
        authorName.setTextColor(appearance.authorNameColor)
        authorName.textSize = appearance.authorNameSize

        message.setTextColor(appearance.messageColor)
        message.textSize = appearance.messageSize


        replyAuthorName.setTextColor(appearance.replyAuthorNameColor)
        replyAuthorName.textSize = appearance.replyAuthorNameSize

        replyMessage.setTextColor(appearance.replyMessageColor)
        replyMessage.textSize = appearance.replyMessageSize

        replyLine.setBackgroundColor(appearance.replyLineColor)

        isRead.setColorFilter(appearance.isReadColor)
        isSent.setColorFilter(appearance.isSentColor)

        imageViewLeftSwipeActionIcon.setImageDrawable(appearance.getSwipeActionIcon())

    }

    private fun View.applyIncomingConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(contentContainer as ConstraintLayout)
        constraintSet.setHorizontalBias(avatarContainer.id, 0f)
        constraintSet.setHorizontalBias(messageContainer.id, 0f)
        constraintSet.connect(messageContainer.id, ConstraintSet.START, avatarContainer.id, ConstraintSet.END)
        constraintSet.applyTo(contentContainer)

        val constraintSet2 = ConstraintSet()
        constraintSet2.clone(messageContainer as ConstraintLayout)
        constraintSet2.setHorizontalBias(authorName.id, 0f)
        constraintSet2.applyTo(messageContainer)
    }

    private fun View.applyOutgoingConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(contentContainer as ConstraintLayout)
        constraintSet.setHorizontalBias(avatarContainer.id, 1f)
        constraintSet.setHorizontalBias(messageContainer.id, 1f)
        constraintSet.connect(messageContainer.id, ConstraintSet.END, avatarContainer.id, ConstraintSet.START)
        constraintSet.applyTo(contentContainer)

        val constraintSet2 = ConstraintSet()
        constraintSet2.clone(messageContainer as ConstraintLayout)
        constraintSet2.setHorizontalBias(authorName.id, 1f)
        constraintSet2.applyTo(messageContainer)
    }

    private fun View.applyOutgoingSelectedAppearance() {
        applyCommonStyle()
        this.messageContainer.background = appearance.getOutgoingSelectedMessageBackground()
    }

    private fun View.applyIncomingSelectedAppearance() {
        applyCommonStyle()
        this.messageContainer.background = appearance.getIncomingSelectedMessageBackground()
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
}