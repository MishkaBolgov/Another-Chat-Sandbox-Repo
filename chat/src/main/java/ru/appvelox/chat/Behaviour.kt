package ru.appvelox.chat

interface Behaviour {
    fun getOnMessageClickListener(): ChatView.OnMessageClickListener?
    fun getOnMessageLongClickListener(): ChatView.OnMessageLongClickListener?
    fun getOnReplyClickListener(): ChatView.OnReplyClickListener?
    fun getOnSwipeActionListener(): ChatView.OnSwipeActionListener?

    fun navigateToRepliedMessage(): Boolean
}