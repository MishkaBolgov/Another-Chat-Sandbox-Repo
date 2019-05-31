package ru.appvelox.chat

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

internal class Appearance: IAppearance {
    override var isReadColor =  Color.argb(255, 255, 255, 255)
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

    override fun getIncomingMessageBackground() = GradientDrawable().apply {
            setColor(incomingMessageBackgroundColor)
            val radius = messageBackgroundCornerRadius
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
        }


    override fun getOutgoingMessageBackground() = GradientDrawable().apply {
            setColor(outgoingMessageBackgroundColor)
            val radius = messageBackgroundCornerRadius
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
        }


    override fun getIncomingSelectedMessageBackground() = GradientDrawable().apply {
        setColor(outgoingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    override fun getOutgoingSelectedMessageBackground() = GradientDrawable().apply {
        setColor(incomingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
    }


}