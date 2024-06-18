package com.example.mojaram

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mojaram.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var openAiApi: OpenAiApi
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHostFragment.navController

        val btnNav: BottomNavigationView = findViewById(R.id.bottom_navigation)
        btnNav.setupWithNavController(navController)

        // Initialize Retrofit for OpenAI API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        openAiApi = retrofit.create(OpenAiApi::class.java)

        setupChatRecyclerView()
        setupFloatingActionButton()

        binding.sendButton.setOnClickListener {
            val userMessage = binding.chatEditText.text.toString()
            if (checkInternetConnection(this)) {
                sendMessageToChatbot(userMessage)
                binding.chatEditText.text.clear() // 메시지 전송 후 EditText 비우기
            } else {
                showError("인터넷 연결 안됨")
            }
        }
    }

    private fun setupChatRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = chatAdapter
    }

    private fun setupFloatingActionButton() {
        val floatingActionButton = binding.floatingActionButton
        floatingActionButton.setOnTouchListener(object : View.OnTouchListener {
            var dX: Float = 0.0f
            var dY: Float = 0.0f
            var lastAction: Int = 0

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        dX = view.x - event.rawX
                        dY = view.y - event.rawY
                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        view.y = event.rawY + dY
                        view.x = event.rawX + dX
                        lastAction = event.action
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (lastAction == MotionEvent.ACTION_DOWN) {
                            toggleChatBox()
                        }
                        return true
                    }
                    else -> return false
                }
            }
        })
    }

    private fun toggleChatBox() {
        val chatBox = binding.chatBox
        if (chatBox.visibility == View.GONE) {
            chatBox.visibility = View.VISIBLE
        } else {
            chatBox.visibility = View.GONE
        }
    }

    private fun sendMessageToChatbot(message: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                chatAdapter.addMessage(Message("user", message)) // 사용자 메시지 추가
                withContext(Dispatchers.Main) {
                    chatAdapter.showTypingIndicator() // 작성중 표시 추가
                }

                val request = ChatRequest(
                    model = "gpt-4-turbo",
                    messages = listOf(Message("user", message))
                )
                val response = openAiApi.chatCompletion(request)
                val reply = response.choices.firstOrNull()?.message?.content ?: "No response"

                withContext(Dispatchers.Main) {
                    chatAdapter.removeTypingIndicator() // 작성중 표시 제거
                    chatAdapter.addMessage(Message("bot", reply)) // 챗봇 메시지 추가
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("챗봇 응답 실패")
                }
            }
        }
    }

    private fun showError(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun checkInternetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}

data class ChatRequest(val model: String, val messages: List<Message>)
data class Message(val role: String, val content: String)
data class ChatResponse(val choices: List<Choice>)
data class Choice(val message: Message)

interface OpenAiApi {
    @Headers(" ")
    @POST("v1/chat/completions")
    suspend fun chatCompletion(@Body request: ChatRequest): ChatResponse
}
