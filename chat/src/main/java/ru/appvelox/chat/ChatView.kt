package ru.appvelox.chat

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import ru.appvelox.chat.model.Message

class ChatView(context: Context, attributeSet: AttributeSet) : RecyclerView(context, attributeSet) {

    private val adapter = MessageAdapter(Appearance())

    fun setOnItemClickListener(listener: OnItemClickListener?) {
        adapter.onItemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener?) {
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
        swipeToReplyCallback.listener = object: OnSwipeActionListener{
            override fun onAction(message: Message) {
                Toast.makeText(context, "Reply on message #${message.getId()}", Toast.LENGTH_SHORT).show()
            }
        }
        swipeToReplyCallback.vibrationDuration = 5000

        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ChatView)

        adapter.onReplyClickListener = object: OnReplyClickListener{
            override fun onReplyClick(message: Message) {
                navigateToMessage(message)
            }
        }
    }

    fun setSelectOnClick(b: Boolean) {
        if (b)
            adapter.onItemClickListener = object : OnItemClickListener {
                override fun onClick(message: Message) {
                    adapter.changeMessageSelection(message)
                }
            }
        else {
            adapter.onItemClickListener = null
            adapter.eraseSelectedMessages()
        }
    }

    fun addMessage(message: Message) {
        adapter.addNewMessage(message)
        layoutManager?.scrollToPosition(adapter.getLastMessageIndex())
    }

    fun setCurrentUserId(id: Long) {
        adapter.currentUserId = id
    }

    fun navigateToMessage(message: Message) {
        val scrollTo = adapter.getPositionOfMessage(message)
        layoutManager?.scrollToPosition(scrollTo)
    }

    interface LoadMoreListener {
        fun requestPreviousMessages(count: Int, alreadyLoadedMessagesCount: Int, callback: LoadMoreCallback)
    }

    fun addOldMessages(messages: List<Message>) {
        adapter.addOldMessages(messages)
    }

    fun deleteMessage(message: Message){
        adapter.deleteMessage(message)
    }

    fun updateMessage(message: Message){
        adapter.updateMessage(message)
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

    fun setMessageTextSize(size: Float){
        adapter.appearance.messageSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setAuthorTextSize(size: Float){
        adapter.appearance.authorNameSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setReplyMessageTextSize(size: Float){
        adapter.appearance.replyMessageSize = size
        adapter.notifyAppearanceChanged()
    }

    fun setReplyAuthorTextSize(size: Float){
        adapter.appearance.replyAuthorNameSize = size
        adapter.notifyAppearanceChanged()
    }

   fun setAuthorTextColor(color: Int){
       adapter.appearance.authorNameColor = color
       adapter.notifyAppearanceChanged()
   }
   fun setMessageTextColor(color: Int){
       adapter.appearance.messageColor = color
       adapter.notifyAppearanceChanged()
   }
   fun setReplyAuthorTextColor(color: Int){
       adapter.appearance.replyAuthorNameColor = color
       adapter.notifyAppearanceChanged()
   }
   fun setReplyMessageTextColor(color: Int){
       adapter.appearance.replyMessageColor = color
       adapter.notifyAppearanceChanged()
   }

    fun setIsReadColor(color: Int){
        adapter.appearance.isReadColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setIsSentColor(color: Int){
        adapter.appearance.isSentColor = color
        adapter.notifyAppearanceChanged()
    }

    fun setReplyLineColor(color: Int) {
        adapter.appearance.replyLineColor = color
        adapter.notifyAppearanceChanged()
    }

    fun notifyDatasetChanged(){
        adapter.notifyDataSetChanged()
    }

    interface LoadMoreCallback {
        fun onResult(messages: List<Message>)
    }

    interface OnItemClickListener {
        fun onClick(message: Message)
    }

    interface OnSwipeActionListener{
        fun onAction(message: Message)
    }

    interface OnItemLongClickListener {
        fun onLongClick(message: Message)
    }

    internal interface OnReplyClickListener {
        fun onReplyClick(message: Message)
    }

}
