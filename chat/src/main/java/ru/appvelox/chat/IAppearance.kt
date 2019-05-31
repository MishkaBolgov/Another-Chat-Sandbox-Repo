package ru.appvelox.chat

import android.graphics.drawable.Drawable
import ru.appvelox.chat.model.Message

interface IAppearance {
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

    fun getOutgoingMessageBackground(): Drawable?
    fun getIncomingMessageBackground(): Drawable?
    fun getOutgoingSelectedMessageBackground(): Drawable
    fun getIncomingSelectedMessageBackground(): Drawable
}