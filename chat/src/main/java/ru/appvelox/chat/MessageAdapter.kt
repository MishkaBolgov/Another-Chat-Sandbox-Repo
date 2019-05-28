package ru.appvelox.chat

import android.os.Handler
import android.os.Looper
import android.util.Log
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
import kotlin.math.log
import kotlin.random.Random

class MessageAdapter(val appearance: Appearance) : RecyclerView.Adapter<MessageViewHolder>() {
    var loadMoreListener: ChatView.LoadMoreListener? = null

    var currentUserId: Long? = null

    var oldDataLoading = false

    var onItemClickListener: OnItemClickListener? = null
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    var onItemLongClickListener: OnItemLongClickListener? = null
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    private val messageList = mutableListOf<Message>()
    private val selectedMessageList = mutableListOf<Message>()

    fun addNewMessage(message: Message) {

        messageList.add(message)
        notifyItemInserted(messageList.indexOf(message))
    }

    fun addOldMessage(messages: List<Message>) {
        messages.forEach { message ->
            messageList.add(0, message)
        }

        Handler(Looper.getMainLooper()).post {

            notifyItemRangeInserted(
                messageList.indexOf(messages.last()),
                messages.size
            )
            oldDataLoading = false
        }
    }

    fun addSelectedMessage(message: Message) {
        if (selectedMessageList.contains(message))
            selectedMessageList.remove(message)
        else
            selectedMessageList.add(message)

        notifyItemChanged(messageList.indexOf(message), null)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)

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

        onItemClickListener?.let { listener ->
            holder.itemView.contentContainer.setOnClickListener {
                listener.onClick(message)
            }
        }

        onItemLongClickListener?.let { listener ->
            holder.itemView.contentContainer.setOnLongClickListener {
                listener.onClick(message)
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
        applyOutgoingConstraints()
        appearance.outgoingDeselectedMessageBackground.constantState?.let {
            this.contentContainer.background = it.newDrawable().mutate()
        }
    }

    private fun View.applyIncomingAppearance() {
        applyIncomingConstraints()
        appearance.incomingDeselectedMessageBackground.constantState?.let {
            this.contentContainer.background = it.newDrawable().mutate()
        }
    }

    private fun View.applyIncomingConstraints(){
        val constraintSet = ConstraintSet()
        constraintSet.clone(messageContainer as ConstraintLayout)
        constraintSet.setHorizontalBias(avatarContainer.id, 0f)
        constraintSet.setHorizontalBias(contentContainer.id, 0f)
        constraintSet.connect(contentContainer.id, ConstraintSet.START, avatarContainer.id, ConstraintSet.END)
        constraintSet.applyTo(messageContainer)

        val constraintSet2 = ConstraintSet()
        constraintSet2.clone(contentContainer as ConstraintLayout)
        constraintSet2.setHorizontalBias(authorName.id, 0f)
        constraintSet2.applyTo(contentContainer)
    }

    private fun View.applyOutgoingConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(messageContainer as ConstraintLayout)
        constraintSet.setHorizontalBias(avatarContainer.id, 1f)
        constraintSet.setHorizontalBias(contentContainer.id, 1f)
        constraintSet.connect(contentContainer.id, ConstraintSet.END, avatarContainer.id, ConstraintSet.START)
        constraintSet.applyTo(messageContainer)

        val constraintSet2 = ConstraintSet()
        constraintSet2.clone(contentContainer as ConstraintLayout)
        constraintSet2.setHorizontalBias(authorName.id, 1f)
        constraintSet2.applyTo(contentContainer)
    }

        private fun View.applyOutgoingSelectedAppearance() {
        appearance.outgoingSelectedMessageBackground.constantState?.let {
            this.contentContainer.background = it.newDrawable().mutate()
        }
    }

    private fun View.applyIncomingSelectedAppearance() {
        appearance.incomingSelectedMessageBackground.constantState?.let {
            this.contentContainer.background = it.newDrawable().mutate()
        }
    }

    fun requestPreviousMessagesFromListener() {
        loadMoreListener?.requestPreviousMessages(20, messageList.size, object : ChatView.LoadMoreCallback {
            override fun onResult(messages: List<Message>) {
                oldDataLoading = true
                addOldMessage(messages)
            }
        })
    }


}