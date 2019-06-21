package ru.appvelox.chat

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import ru.appvelox.chat.model.TextMessage

class DefaultMessageAdapter(appearance: ChatAppearance, initTextMessages: List<TextMessage>? = null): MessageAdapter(appearance, initTextMessages) {

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


        val isRead = findViewById<View>(R.id.isRead)
        val avatarContainer = findViewById<View>(R.id.avatarContainer)
        val authorName = findViewById<View>(R.id.authorName)
        val replyAuthorName = findViewById<View?>(R.id.replyAuthorName)
        val messageContainer = findViewById<View>(R.id.messageContainer)

        isRead.visibility = View.VISIBLE
        avatarContainer.visibility = if (appearance.isOutgoingAvatarVisible) View.VISIBLE else View.GONE
        authorName.visibility = if (appearance.isOutgoingAuthorNameVisible) View.VISIBLE else View.GONE
        replyAuthorName?.visibility = if (appearance.isOutgoingAuthorNameVisible) View.VISIBLE else View.GONE
        messageContainer.background = appearance.getOutgoingMessageBackground()
    }

    private fun View.applyIncomingAppearance() {
        applyCommonStyle()
        applyIncomingConstraints()

        val isRead = findViewById<View>(R.id.isRead)
        val avatarContainer = findViewById<View>(R.id.avatarContainer)
        val authorName = findViewById<View>(R.id.authorName)
        val replyAuthorName = findViewById<View?>(R.id.replyAuthorName)
        val messageContainer = findViewById<View>(R.id.messageContainer)

        isRead.visibility = View.GONE
        avatarContainer.visibility = if (appearance.isIncomingAvatarVisible) View.VISIBLE else View.GONE
        authorName.visibility = if (appearance.isIncomingAuthorNameVisible) View.VISIBLE else View.GONE
        replyAuthorName?.visibility = if (appearance.isIncomingAuthorNameVisible) View.VISIBLE else View.GONE
        messageContainer.background = appearance.getIncomingMessageBackground()
    }

    private fun View.applyCommonStyle() {

        val date = findViewById<TextView>(R.id.date)
        val authorName = findViewById<TextView>(R.id.authorName)
        val message = findViewById<TextView?>(R.id.message)
        val time = findViewById<TextView>(R.id.time)
        val replyAuthorName = findViewById<TextView?>(R.id.replyAuthorName)
        val replyMessage = findViewById<TextView?>(R.id.replyMessage)
        val replyLine = findViewById<View?>(R.id.replyLine)

        val isRead = findViewById<ImageView>(R.id.isRead)
        val isSent = findViewById<ImageView>(R.id.isSent)
        val imageViewLeftSwipeActionIcon = findViewById<ImageView>(R.id.imageViewLeftSwipeActionIcon)
        val messageContainer = findViewById<ConstraintLayout>(R.id.messageContainer)

        date.setTextColor(appearance.dateTextColor)
        date.textSize = appearance.dateTextSize

        authorName.setTextColor(appearance.authorNameColor)
        authorName.textSize = appearance.authorNameSize

        message?.setTextColor(appearance.messageColor)
        message?.textSize = appearance.messageTextSize

        time.setTextColor(appearance.timeTextColor)
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

    private fun View.applyIncomingConstraints() {
        val contentContainer = findViewById<View>(R.id.contentContainer)
        val avatarContainer = findViewById<View>(R.id.avatarContainer)
        val messageContainer = findViewById<View>(R.id.messageContainer)
        val authorName = findViewById<View>(R.id.authorName)

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
        val contentContainer = findViewById<View>(R.id.contentContainer)
        val avatarContainer = findViewById<View>(R.id.avatarContainer)
        val messageContainer = findViewById<View>(R.id.messageContainer)
        val authorName = findViewById<View>(R.id.authorName)

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
        val messageContainer = findViewById<View>(R.id.messageContainer)
        messageContainer.background = appearance.getOutgoingSelectedMessageBackground()
    }

    private fun View.applyIncomingSelectedAppearance() {
        applyCommonStyle()
        val messageContainer = findViewById<View>(R.id.messageContainer)
        messageContainer.background = appearance.getIncomingSelectedMessageBackground()
    }

}