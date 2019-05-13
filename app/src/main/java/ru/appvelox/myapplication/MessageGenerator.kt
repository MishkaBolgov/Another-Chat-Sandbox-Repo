package ru.appvelox.myapplication

import android.graphics.Bitmap
import ru.appvelox.chat.model.Author
import ru.appvelox.chat.model.Message
import java.lang.StringBuilder
import java.util.*
import kotlin.random.Random

object MessageGenerator {

    val user1 = object : Author {
        override fun getName() = "Golfred"

        override fun getId() = 0L

        override fun getAvatar(): String {
            return "https://media.pitchfork.com/photos/5929bbb45e6ef95969322c92/1:1/w_320/c91b3bfd.jpg"
        }
    }

    val user2 = object : Author {
        override fun getName() = "Gretta Colossal"

        override fun getId() = 1L

        override fun getAvatar(): String {
            return "https://media.pitchfork.com/photos/5929bbb45e6ef95969322c92/1:1/w_320/c91b3bfd.jpg"
        }
    }

    var nextId = 0L
        get() = field++

    var nextDate = Date()
    get() {
        val currentDate = field
        field = Date(field.time + Random.nextLong(44_000_000))
        return currentDate
    }

    fun generateMessage(): Message {
        return object : Message {

            private val mId = nextId
            private val mUser = if (Random.nextBoolean()) user1 else user2
            private val mMessageText = MessageGenerator.generateMessageText(Random.nextInt(20))
            private val mDate = nextDate

            init {
                ++MainActivity.counter
            }

            override fun getId(): Long {
                return mId
            }

            override fun getText(): String {
                return mMessageText
            }

            override fun getAuthor(): Author {
                return mUser
            }

            override fun getDate(): Date {
                return mDate
            }
        }
    }


    fun generateMessageText(words: Int = 20): String {
        val wordNumber = wordsArray.size
        val message = StringBuilder()
        repeat(words + 1) {
            message.append(wordsArray[Random.nextInt(wordNumber)])
            message.append(" ")

        }
        return message.toString().toLowerCase().capitalize()
    }

    val wordsArray = "One evening in January 1842, Virginia showed the first signs of consumption, now known as tuberculosis, while singing and playing the piano, which Poe described as breaking a blood vessel in her throat. She only partially recovered, and Poe began to drink more heavily under the stress of her illness. He left Graham's and attempted to find a new position, for a time angling for a government post. He returned to New York where he worked briefly at the Evening Mirror before becoming editor of the Broadway Journal, and later its owner. There he alienated himself from other writers by publicly accusing Henry Wadsworth Longfellow of plagiarism, though Longfellow never responded. On January 29, 1845, his poem \"The Raven\" appeared in the Evening Mirror and became a popular sensation. It made Poe a household name almost instantly, though he was paid only \$9 for its publication. It was concurrently published in The American Review: A Whig Journal under the pseudonym \"Quarles\".".split(' ')
}