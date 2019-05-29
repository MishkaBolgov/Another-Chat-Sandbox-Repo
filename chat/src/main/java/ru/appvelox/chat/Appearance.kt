package ru.appvelox.chat

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

internal class Appearance {
    var messageBackgroundCornerRadius = 0f
    var incomingMessageBackgroundColor = Color.argb(20, 33, 150, 243)
        set(value) {
            field = value
            incomingMessageBackground = createIncomingMessageBackground()
        }

    var outgoingMessageBackgroundColor = Color.argb(20, 76, 175, 80)
        set(value) {
            field = value
            outgoingMessageBackground = createOutgoingMessageBackground()
        }

    var incomingSelectedMessageBackgroundColor = Color.argb(20, 98, 50, 43)
    var outgoingSelectedMessageBackgroundColor = Color.argb(20, 98, 75, 180)

    var replyAuthorNameColor = Color.DKGRAY
    var replyMessageColor = Color.DKGRAY
    var replyLineColor = Color.CYAN
    var replyAuthorNameSize: Float = 15f
    var replyMessageSize: Float = 14f

    var authorNameColor = Color.GRAY
    var messageColor = Color.BLUE
    var authorNameSize: Float = 16f
    var messageSize: Float = 15f

    var incomingMessageBackground = createIncomingMessageBackground()

    private fun createIncomingMessageBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(outgoingMessageBackgroundColor)
            val radius = messageBackgroundCornerRadius
            cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
        }
    }

    var outgoingMessageBackground = createOutgoingMessageBackground()

    private fun createOutgoingMessageBackground(): GradientDrawable {
        return GradientDrawable().apply {
            setColor(incomingMessageBackgroundColor)
            val radius = messageBackgroundCornerRadius
            cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
        }
    }

    val incomingSelectedMessageBackground = GradientDrawable().apply {
        setColor(outgoingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    val outgoingSelectedMessageBackground = GradientDrawable().apply {
        setColor(incomingSelectedMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
    }

    val incomingMessageStateBackground: Drawable
        get() {
            val background = StateListDrawable()
            background.addState(intArrayOf(0), incomingMessageBackground)
            background.addState(intArrayOf(1), incomingSelectedMessageBackground)

            return background
        }

}