package ru.appvelox.chat

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import ru.appvelox.chat.model.ImageMessage
import ru.appvelox.chat.model.Message
import ru.appvelox.chat.model.TextMessage

class DefaultMessageAdapter(appearance: ChatAppearance, initTextMessages: List<TextMessage>? = null) :
    MessageAdapter(appearance, initTextMessages) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val viewHolder = super.onCreateViewHolder(parent, viewType)

        when (viewType) {
            MessageType.INCOMING.type -> applyIncomingAppearance(viewHolder.itemView, MessageType.INCOMING)
            MessageType.OUTGOING.type -> applyOutgoingAppearance(viewHolder.itemView, MessageType.OUTGOING)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val message = messageList[position]

        if (selectedMessageList.contains(message))
            if (message.isIncoming())
                applyIncomingSelectedAppearance(holder.itemView, MessageType.INCOMING)
            else
                applyOutgoingSelectedAppearance(holder.itemView, MessageType.OUTGOING)
        else
            if (message.isIncoming())
                applyIncomingAppearance(holder.itemView, MessageType.INCOMING, message)
            else
                applyOutgoingAppearance(holder.itemView, MessageType.OUTGOING, message)
    }

    private fun applyOutgoingAppearance(view: View, messageType: MessageType, message: Message? = null) {
        applyCommonStyle(view, messageType, message)
        applyOutgoingConstraints(view, messageType)


        val isRead = view.findViewById<View>(R.id.isRead)
        val avatarContainer = view.findViewById<View>(R.id.avatarContainer)
        val authorName = view.findViewById<View>(R.id.authorName)
        val replyAuthorName = view.findViewById<View?>(R.id.replyAuthorName)
        val messageContainer = view.findViewById<View>(R.id.messageContainer)

        isRead.visibility = View.VISIBLE
        avatarContainer.visibility = if (appearance.isOutgoingAvatarVisible) View.VISIBLE else View.GONE
        authorName.visibility = if (appearance.isOutgoingAuthorNameVisible) View.VISIBLE else View.GONE
        replyAuthorName?.visibility = if (appearance.isOutgoingAuthorNameVisible) View.VISIBLE else View.GONE
        messageContainer.background = appearance.getOutgoingMessageBackground()
    }

    private fun applyIncomingAppearance(view: View, messageType: MessageType, message: Message? = null) {
        applyCommonStyle(view, messageType, message)
        applyIncomingConstraints(view, messageType)

        val isRead = view.findViewById<View>(R.id.isRead)
        val avatarContainer = view.findViewById<View>(R.id.avatarContainer)
        val authorName = view.findViewById<View>(R.id.authorName)
        val replyAuthorName = view.findViewById<View?>(R.id.replyAuthorName)
        val messageContainer = view.findViewById<View>(R.id.messageContainer)

        isRead.visibility = View.GONE
        avatarContainer.visibility = if (appearance.isIncomingAvatarVisible) View.VISIBLE else View.GONE
        authorName.visibility = if (appearance.isIncomingAuthorNameVisible) View.VISIBLE else View.GONE
        replyAuthorName?.visibility = if (appearance.isIncomingAuthorNameVisible) View.VISIBLE else View.GONE
        messageContainer.background = appearance.getIncomingMessageBackground()
    }

    private fun applyCommonStyle(view: View, messageType: MessageType, message: Message? = null) {

        val date = view.findViewById<TextView>(R.id.date)
        val authorName = view.findViewById<TextView>(R.id.authorName)
        val messageText = view.findViewById<TextView?>(R.id.message)
        val time = view.findViewById<TextView>(R.id.time)
        val replyAuthorName = view.findViewById<TextView?>(R.id.replyAuthorName)
        val replyMessage = view.findViewById<TextView?>(R.id.replyMessage)
        val replyLine = view.findViewById<View?>(R.id.replyLine)

        val isRead = view.findViewById<ImageView>(R.id.isRead)
        val isSent = view.findViewById<ImageView>(R.id.isSent)
        val imageViewLeftSwipeActionIcon = view.findViewById<ImageView>(R.id.imageViewLeftSwipeActionIcon)
        val messageContainer = view.findViewById<ConstraintLayout>(R.id.messageContainer)

        date.setTextColor(appearance.dateTextColor)
        date.textSize = appearance.dateTextSize

        authorName.setTextColor(appearance.authorNameColor)
        authorName.textSize = appearance.authorNameSize

        messageText?.setTextColor(appearance.messageColor)
        messageText?.textSize = appearance.messageTextSize

        time.setTextColor(appearance.timeTextColor)

//        todo: херня! Исправить!
        if (message is ImageMessage) {
            time.setTextColor(appearance.imageTimeTextColor)
            val timeBackground = view.findViewById<ViewGroup>(R.id.timeContainer)
            timeBackground.background = appearance.getTimeBackground()
        }

        time.textSize = appearance.timeTextSize

        replyAuthorName?.setTextColor(appearance.replyAuthorNameColor)
        replyAuthorName?.textSize = appearance.replyAuthorNameSize

        replyMessage?.setTextColor(appearance.replyMessageColor)
        replyMessage?.textSize = appearance.replyMessageSize

        replyLine?.setBackgroundColor(appearance.replyLineColor)

        isRead.setColorFilter(appearance.isReadColor)
        isSent.setColorFilter(appearance.isSentColor)

        imageViewLeftSwipeActionIcon.setImageDrawable(appearance.getSwipeActionIcon())

        messageContainer.maxWidth = appearance.maxMessageWidth
        messageContainer.minWidth = appearance.minMessageWidth
    }

    private fun applyIncomingConstraints(view: View, messageType: MessageType) {
        val contentContainer = view.findViewById<View>(R.id.contentContainer)
        val avatarContainer = view.findViewById<View>(R.id.avatarContainer)
        val messageContainer = view.findViewById<View>(R.id.messageContainer)
        val authorName = view.findViewById<View>(R.id.authorName)

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

    private fun applyOutgoingConstraints(view: View, messageType: MessageType) {
        val contentContainer = view.findViewById<View>(R.id.contentContainer)
        val avatarContainer = view.findViewById<View>(R.id.avatarContainer)
        val messageContainer = view.findViewById<View>(R.id.messageContainer)
        val authorName = view.findViewById<View>(R.id.authorName)

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

    private fun applyOutgoingSelectedAppearance(view: View, messageType: MessageType) {
        applyCommonStyle(view, messageType)
        val messageContainer = view.findViewById<View>(R.id.messageContainer)
        messageContainer.background = appearance.getOutgoingSelectedMessageBackground()
    }

    private fun applyIncomingSelectedAppearance(view: View, messageType: MessageType) {
        applyCommonStyle(view, messageType)
        val messageContainer = view.findViewById<View>(R.id.messageContainer)
        messageContainer.background = appearance.getIncomingSelectedMessageBackground()
    }

}