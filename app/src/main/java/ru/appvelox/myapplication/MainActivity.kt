package ru.appvelox.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import ru.appvelox.chat.ChatView
import ru.appvelox.chat.MessageAdapter
import ru.appvelox.chat.MessageViewHolder
import ru.appvelox.chat.model.Author
import ru.appvelox.chat.model.Message
import java.util.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    companion object {
        var counter = 0
    }

    val listener = object : ChatView.LoadMoreListener {
        override fun requestPreviousMessages(
            count: Int,
            alreadyLoadedMessagesCount: Int,
            callback: ChatView.LoadMoreCallback
        ) {
            val messages = mutableListOf<Message>()
            repeat(count) {
                messages.add(MessageGenerator.generateMessage())
            }
            AsyncTask.execute {
                Thread.sleep(1000)
                callback.onResult(messages)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatView.setLoadMoreListener(listener)
        chatView.setCurrentUserId(MessageGenerator.user1.getId())

        button1.setOnClickListener {
            chatView.addMessage(MessageGenerator.generateMessage())
        }

//        button2.setOnClickListener {
//            val messages = mutableListOf<Message>()
//            repeat(5) {
//                messages.add(MessageGenerator.generateMessage())
//            }
//            chatView.addOldMessages(messages)
//        }
    }
}