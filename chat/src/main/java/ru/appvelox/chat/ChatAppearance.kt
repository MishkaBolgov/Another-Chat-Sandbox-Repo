package ru.appvelox.chat

import android.graphics.drawable.Drawable

interface ChatAppearance {
    var outgoingSelectedMessageBackgroundColor: Int
    var incomingSelectedMessageBackgroundColor: Int
    var outgoingMessageBackgroundColor: Int
    var incomingMessageBackgroundColor: Int
    var messageBackgroundCornerRadius: Float
    var isSentColor: Int
    var isReadColor: Int
    var replyLineColor: Int
    var replyMessageSize: Float
    var replyMessageColor: Int
    var replyAuthorNameSize: Float
    var replyAuthorNameColor: Int
    var messageSize: Float
    var messageColor: Int
    var authorNameSize: Float
    var authorNameColor: Int

    var isIncomingAvatarVisible: Boolean
    var isOutgoingAvatarVisible: Boolean
    var isIncomingAuthorNameVisible: Boolean
    var isOutgoingAuthorNameVisible: Boolean
    var isIncomingReplyAuthorNameVisible: Boolean
    var isOutgoingReplyAuthorNameVisible: Boolean

    fun getOutgoingMessageBackground(isInChain: Boolean = false): Drawable?
    fun getIncomingMessageBackground(isInChain: Boolean = false): Drawable?
    fun getOutgoingSelectedMessageBackground(isInChain: Boolean = false): Drawable
    fun getIncomingSelectedMessageBackground(isInChain: Boolean = false): Drawable
    fun getSwipeActionIcon(): Drawable?

}