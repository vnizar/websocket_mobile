package com.voen.socketapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MainActivity : AppCompatActivity(), AppWebSocket.AppWebSocketCallback {
    companion object {
        private const val TAG = "voenxyz"
    }

    private val client = AppWebSocket(URI("wss://lit-refuge-24811.herokuapp.com"), this)
    private var chatId: String? = null
    private val chatList: MutableList<ChatMessage> = mutableListOf()
    private val chatAdapter by lazy {
        ChatRecyclerViewAdapter(chatList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupView()
        setupListeners()
        initWebSocket()
    }

    private fun setupListeners() {
        bt_send.setOnClickListener {
            client.send(et_chat.text.toString())
            et_chat.text.clear()
        }
    }

    private fun setupView() {
        with(rv_chat) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = chatAdapter
        }
    }

    private fun initWebSocket() {
        client.connect()
    }

    override fun onOpen(handshakedata: ServerHandshake?) {
        Log.d(TAG, "On Open")
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        Log.d(TAG, "On Close")
    }

    override fun onMessage(message: String?) {
        Log.d(TAG, "On Message : $message")
        when (val result = parseMessageToJSON(message)) {
            is ChatMessage -> {
                Log.d(TAG, "chat : ${result.message}")
                when (result.type) {
                    "user_id" -> {
                        chatId = result.message
                    }
                    "chat" -> {
                        runOnUiThread {
                            chatList.add(result)
                            chatAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
            is ChatUsers -> {
                Log.d(TAG, "users : ${result.message}")
            }
        }
    }

    override fun onError(ex: Exception?) {
        Log.d(TAG, "On Error : $ex")
    }

    private fun parseMessageToJSON(message: String?): Any? {
        if (message.isNullOrEmpty()) return null

        val gson = Gson()
        return try {
            val chatType = gson.fromJson(message, ChatType::class.java)
            gson.fromJson(
                message,
                when (chatType.type) {
                    "user_id", "chat" -> {
                        ChatMessage::class.java
                    }
                    else -> {
                        ChatUsers::class.java
                    }
                }
            )
        } catch (e: Exception) {
            null
        }
    }
}
