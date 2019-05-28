package ru.appvelox.chat

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable

class Appearance {
    var messageBackgroundCornerRadius = 30f
    var incomingMessageBackgroundColor = Color.argb(20, 33, 150, 243)
    var outgoingMessageBackgroundColor = Color.argb(20, 76, 175, 80)
    var incomingSelectedMessageBackgroundColor = Color.argb(20, 98, 50, 43)
    var outgoingSelectedMessageBackgroundColor = Color.argb(20, 98, 75, 180)
    var replyAuthorNameColor = Color.DKGRAY

    val incomingDeselectedMessageBackground = GradientDrawable().apply {
        setColor(outgoingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(0f, 0f, radius, radius, radius, radius, radius, radius)
    }

    val outgoingDeselectedMessageBackground = GradientDrawable().apply {
        setColor(incomingMessageBackgroundColor)
        val radius = messageBackgroundCornerRadius
        cornerRadii = floatArrayOf(radius, radius, 0f, 0f, radius, radius, radius, radius)
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
        background.addState(intArrayOf(0), incomingDeselectedMessageBackground)
        background.addState(intArrayOf(1), incomingSelectedMessageBackground)

        return background
    }

}