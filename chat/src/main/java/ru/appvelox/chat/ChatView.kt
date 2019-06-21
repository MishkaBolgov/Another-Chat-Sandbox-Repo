package ru.appvelox.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.appvelox.chat.model.Author
import ru.appvelox.chat.model.Message
import ru.appvelox.chat.model.TextMessage
import java.util.*

class ChatView(context: Context, attributeSet: AttributeSet) : RecyclerView(context, attributeSet) {

    private var adapter: MessageAdapter = DefaultMessageAdapter(DefaultAppearance(context))

    fun setOnItemClickListener(listener: OnMessageClickListener?) {
        adapter.onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnMessageLongClickListener?) {
        adapter.onItemLongClickListener = listener
    }

    fun setLoadMoreListener(listener: LoadMoreListener?) {
        adapter.loadMoreListener = listener
    }

    init {
        super.setAdapter(adapter)
        val layoutManager = MessageLayoutManager(context)
        super.setLayoutManager(layoutManager)
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (layoutManager.findFirstVisibleItemPosition() <= 5 && !adapter.oldDataLoading) {
                    adapter.requestPreviousMessagesFromListener()
                }
            }
        })
        val swipeToReplyCallback = SwipeToReplyCallback()
        val itemTouchHelper = ItemTouchHelper(swipeToReplyCallback)
        itemTouchHelper.attachToRecyclerView(this)
        swipeToReplyCallback.listener = object : OnSwipeActionListener {
            override fun onAction(textMessage: Message) {
                Toast.makeText(context, "Reply on textMessage #${textMessage.getId()}", Toast.LENGTH_SHORT).show()
            }
        }

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ChatView)

        adapter.onReplyClickListener = object : OnReplyClickListener {
            override fun onReplyClick(textMessage: Message) {
                navigateToMessage(textMessage)
            }
        }
    }

    fun setSelectOnClick(b: Boolean) {
        if (b)
            adapter.onItemClickListener = object : OnMessageClickListener {
                override fun onClick(textMessage: Message) {
                    adapter.changeMessageSelection(textMessage)
                }
            }
        else {
            adapter.onItemClickListener = null
            adapter.eraseSelectedMessages()
        }
    }

    fun addMessage(textMessage: Message) {
        adapter.addNewMessage(textMessage)
        layoutManager?.scrollToPosition(adapter.getLastMessageIndex())
    }

    fun setCurrentUserId(id: Long) {
        adapter.currentUserId = id
    }

    fun navigateToMessage(textMessage: Message) {
        val scrollTo = adapter.getPositionOfMessage(textMessage)
        layoutManager?.scrollToPosition(scrollTo)
    }

    interface LoadMoreListener {
        fun requestPreviousMessages(count: Int, alreadyLoadedMessagesCount: Int, callback: LoadMoreCallback)
    }

    fun addOldMessages(textMessages: List<TextMessage>) {
        adapter.addOldMessages(textMessages)
    }

    fun deleteMessage(textMessage: TextMessage) {
        adapter.deleteMessage(textMessage)
    }

    fun updateMessage(textMessage: TextMessage) {
        adapter.updateMessage(textMessage)
    }

    fun setMessageBackgroundCornerRadius(radius: Float) {
        adapter.appearance.messageBackgroundCornerRadius = radius
        adapter.notifyAppearanceChanged()
    }

    fun setIncomingMessageBackgroundColor(color: Int) {
        adapter.appearance.incomingMessageBackgroundColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setOutgoingMessageBackgroundColor(color: Int) {
        adapter.appearance.outgoingMessageBackgroundColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setIncomingSelectedMessageBackgroundColor(color: Int) {
        adapter.appearance.incomingSelectedMessageBackgroundColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setOutgoingSelectedMessageBackgroundColor(color: Int) {
        adapter.appearance.outgoingSelectedMessageBackgroundColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setMessageTextSize(size: Float) {
        adapter.appearance.messageTextSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setAuthorTextSize(size: Float) {
        adapter.appearance.authorNameSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setReplyMessageTextSize(size: Float) {
        adapter.appearance.replyMessageSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setReplyAuthorTextSize(size: Float) {
        adapter.appearance.replyAuthorNameSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setAuthorTextColor(color: Int) {
        adapter.appearance.authorNameColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setMessageTextColor(color: Int) {
        adapter.appearance.messageColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setReplyAuthorTextColor(color: Int) {
        adapter.appearance.replyAuthorNameColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setReplyMessageTextColor(color: Int) {
        adapter.appearance.replyMessageColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setIsReadColor(color: Int) {
        adapter.appearance.isReadColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setIsSentColor(color: Int) {
        adapter.appearance.isSentColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setReplyLineColor(color: Int) {
        adapter.appearance.replyLineColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setMaxWidth(width: Int) {
        adapter.appearance.maxMessageWidth = width
        adapter.notifyAppearanceChanged()
    }

    fun setMinWidth(width: Int) {
        adapter.appearance.minMessageWidth = width
        adapter.notifyAppearanceChanged()
    }

    fun notifyDatasetChanged() {
        adapter.notifyDataSetChanged()
    }

    fun addMessages(textMessages: MutableList<TextMessage>) {
        adapter.addMessages(textMessages)
    }

    fun setLayout(incomingMessageLayout: Int?, outgoingMessageLayout: Int?) {
        val currentAppearance = adapter.appearance
        val oldAdapter = adapter

        if (incomingMessageLayout == null || outgoingMessageLayout == null)
            adapter = DefaultMessageAdapter(currentAppearance)
        else
            adapter = MessageAdapter(currentAppearance)

        oldAdapter.copyPropertiesTo(adapter)

        setAdapter(adapter)

        (adapter.appearance as DefaultAppearance).setMessageLayout(incomingMessageLayout, outgoingMessageLayout)
        adapter.notifyAppearanceChanged()
    }


    interface LoadMoreCallback {
        fun onResult(textMessages: List<Message>)
    }

    interface OnMessageClickListener {
        fun onClick(textMessage: Message)
    }

    interface OnReplyClickListener {
        fun onReplyClick(textMessage: Message)
    }

    interface OnSwipeActionListener {
        fun onAction(textMessage: Message)
    }

    interface OnMessageLongClickListener {
        fun onLongClick(textMessage: Message)
    }

    interface OnAvatarClickListener {
        fun onClick(author: Author)
    }

    interface DateFormatter {
        fun formatDate(date: Date): String
        fun formatTime(date: Date): String
    }
}
