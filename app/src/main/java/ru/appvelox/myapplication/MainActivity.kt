package ru.appvelox.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatView.setCurrentUserId(MessageGenerator.user1.getId())

        button.setOnClickListener {
            chatView.addMessage(MessageGenerator.generateMessage())
        }
    }
}

