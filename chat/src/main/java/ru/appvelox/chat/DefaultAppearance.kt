package ru.appvelox.chat

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable

internal class DefaultAppearance(val context: Context) : ChatAppearance {
    override var isReadColor = Color.argb(255, 255, 255, 255)
    override var isSentColor = Color.argb(255, 255, 255, 255)

    override var messageBackgroundCornerRadius = 0f
    override var incomingMessageBackgroundColor = Color.argb(255, 175, 224, 255)

    override var outgoingMessageBackgroundColor = Color.argb(255, 192, 221, 232)

    override var incomingSelectedMessageBackgroundColor = Color.argb(255, 98, 50, 43)
    override var outgoingSelectedMessageBackgroundColor = Color.argb(255, 98, 75, 180)

    override var replyAuthorNameColor = Color.DKGRAY
    override var replyMessageColor = Color.DKGRAY
    override var replyLineColor = Color.CYAN
    override var replyAuthorNameSize: Float = 15f
    override var replyMessageSize: Float = 14f

    override var authorNameColor = Color.GRAY
    override var messageColor = Color.BLUE
    override var authorNameSize: Float = 16f
    override var messageSize: Float = 15f

    override var isIncomingAvatarVisible = false
    override var isOutgoingAvatarVisible = false
    override var isIncomingAuthorNameVisible = true
    override var isOutgoingAuthorNameVisible = false
    override var isIncomingReplyAuthorNameVisible = false
    override var isOutgoingReplyAuthorNameVisible = true

    override fun getIncomingMessageBackground(isInChain: Boolean) = GradientDrawable().apply {
        setColor(incomingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        if (isInChain)
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
        else
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }


    override fun getOutgoingMessageBackground(isInChain: Boolean) = GradientDrawable().apply {
        setColor(outgoingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        if (isInChain)
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
        else
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
    }


    override fun getIncomingSelectedMessageBackground(isInChain: Boolean) = GradientDrawable().apply {
        setColor(outgoingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        if (isInChain)
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, 0f, 0f)
        else
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    override fun getOutgoingSelectedMessageBackground(isInChain: Boolean) = GradientDrawable().apply {
        setColor(incomingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        if (isInChain)
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, 0f, 0f, radius, radius)
        else
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
    }

    var swipeActionIconResource = R.drawable.ic_reply_black_24dp

    override fun getSwipeActionIcon(): Drawable? {
        return context.resources.getDrawable(swipeActionIconResource)
    }
}