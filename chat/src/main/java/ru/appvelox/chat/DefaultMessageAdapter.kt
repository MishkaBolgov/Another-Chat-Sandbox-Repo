package ru.appvelox.chat

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.android.synthetic.main.item_message.view.*
import ru.appvelox.chat.model.Message

class DefaultMessageAdapter(appearance: ChatAppearance, initMessages: List<Message>? = null): MessageAdapter(appearance, initMessages) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val viewHolder = super.onCreateViewHolder(parent, viewType)

        when (viewType) {
            MessageType.INCOMING.type -> viewHolder.itemView.applyIncomingAppearance()
            MessageType.OUTGOING.type -> viewHolder.itemView.applyOutgoingAppearance()
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)

        val message = messageList[position]

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

    private fun View.applyOutgoingAppearance() {
        applyCommonStyle()
        applyOutgoingConstraints()
        isRead.visibility = View.VISIBLE
        avatarContainer.visibility = if (appearance.isOutgoingAvatarVisible) View.VISIBLE else View.GONE
        authorName.visibility = if (appearance.isOutgoingAuthorNameVisible) View.VISIBLE else View.GONE
        replyAuthorName.visibility = if (appearance.isOutgoingAuthorNameVisible) View.VISIBLE else View.GONE
        this.messageContainer.background = appearance.getOutgoingMessageBackground()
    }

    private fun View.applyIncomingAppearance() {
        applyCommonStyle()
        applyIncomingConstraints()
        isRead.visibility = View.GONE
        avatarContainer.visibility = if (appearance.isIncomingAvatarVisible) View.VISIBLE else View.GONE
        authorName.visibility = if (appearance.isIncomingAuthorNameVisible) View.VISIBLE else View.GONE
        replyAuthorName.visibility = if (appearance.isIncomingAuthorNameVisible) View.VISIBLE else View.GONE
        this.messageContainer.background = appearance.getIncomingMessageBackground()
    }

    private fun View.applyCommonStyle() {

        date.setTextColor(appearance.dateTextColor)
        date.textSize = appearance.dateTextSize

        authorName.setTextColor(appearance.authorNameColor)
        authorName.textSize = appearance.authorNameSize

        message.setTextColor(appearance.messageColor)
        message.textSize = appearance.messageTextSize

        time.setTextColor(appearance.timeTextColor)
        time.textSize = appearance.timeTextSize

        replyAuthorName.setTextColor(appearance.replyAuthorNameColor)
        replyAuthorName.textSize = appearance.replyAuthorNameSize

        replyMessage.setTextColor(appearance.replyMessageColor)
        replyMessage.textSize = appearance.replyMessageSize

        replyLine.setBackgroundColor(appearance.replyLineColor)

        isRead.setColorFilter(appearance.isReadColor)
        isSent.setColorFilter(appearance.isSentColor)

        imageViewLeftSwipeActionIcon.setImageDrawable(appearance.getSwipeActionIcon())

        messageContainer.maxWidth = appearance.maxMessageWidth

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

}