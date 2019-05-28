package ru.appvelox.myapplication

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import ru.appvelox.chat.*
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
                messages.add(MessageGenerator.generateMessage(true))
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


        chatView.addMessage(MessageGenerator.generateMessage(false))
        chatView.addMessage(MessageGenerator.generateMessage(false))
        chatView.addMessage(MessageGenerator.generateMessage(false))

        button1.setOnClickListener {
            chatView.addMessage(MessageGenerator.generateMessage(false))
        }

        button2.setOnClickListener {
            chatView.onItemClickListener = object : OnItemClickListener {
                override fun onClick(message: Message) {
                    Toast.makeText(this@MainActivity, message.getText(), Toast.LENGTH_SHORT).show()
                }
            }
            chatView.onItemLongClickListener = object : OnItemLongClickListener {
                override fun onClick(message: Message) {
                }
            }
        }
    }
}