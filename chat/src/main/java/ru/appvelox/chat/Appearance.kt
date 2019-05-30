package ru.appvelox.chat

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

internal class Appearance {
    var isReadColor =  Color.argb(255, 255, 255, 255)
    var isSentColor = Color.argb(255, 255, 255, 255)

    var messageBackgroundCornerRadius = 0f
    var incomingMessageBackgroundColor = Color.argb(255, 175, 224, 255)

    var outgoingMessageBackgroundColor = Color.argb(255, 192, 221, 232)

    var incomingSelectedMessageBackgroundColor = Color.argb(255, 98, 50, 43)
    var outgoingSelectedMessageBackgroundColor = Color.argb(255, 98, 75, 180)

    var replyAuthorNameColor = Color.DKGRAY
    var replyMessageColor = Color.DKGRAY
    var replyLineColor = Color.CYAN
    var replyAuthorNameSize: Float = 15f
    var replyMessageSize: Float = 14f

    var authorNameColor = Color.GRAY
    var messageColor = Color.BLUE
    var authorNameSize: Float = 16f
    var messageSize: Float = 15f

    fun getIncomingMessageBackground() = GradientDrawable().apply {
            setColor(incomingMessageBackgroundColor)
            val radius = messageBackgroundCornerRadius
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
        }


    fun getOutgoingMessageBackground() = GradientDrawable().apply {
            setColor(outgoingMessageBackgroundColor)
            val radius = messageBackgroundCornerRadius
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
        }


    fun getIncomingSelectedMessageBackground() = GradientDrawable().apply {
        setColor(outgoingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    fun getOutgoingSelectedMessageBackground() = GradientDrawable().apply {
        setColor(incomingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
    }


}