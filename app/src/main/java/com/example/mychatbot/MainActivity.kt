package com.example.mychatbot

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {

    // creating variables for our
    // widgets in xml file.
    private lateinit var chatsRV: RecyclerView
    private lateinit var sendMsgIB: ImageButton
    private lateinit var userMsgEdt: EditText
    private val USER_KEY = "user"
    private val BOT_KEY = "bot"

    // creating a variable for
    // our volley request queue.
    private lateinit var mRequestQueue: RequestQueue

    // creating a variable for array list and adapter class.
    private var messageModalArrayList: ArrayList<MessageModal> = ArrayList()
    private lateinit var messageRVAdapter: MessageRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // on below line we are initializing all our views.
        chatsRV = findViewById(R.id.idRVChats)
        sendMsgIB = findViewById(R.id.idIBSend)
        userMsgEdt = findViewById(R.id.idEdtMessage)

        // below line is to initialize our request queue.
        mRequestQueue = Volley.newRequestQueue(this)
        mRequestQueue.cache.clear()

        // creating a new array list
        messageModalArrayList = ArrayList()

        // adding on click listener for send message button.
        sendMsgIB.setOnClickListener {
            // checking if the message entered
            // by user is empty or not.
            if (userMsgEdt.text.toString().isEmpty()) {
                // if the edit text is empty display a toast message.
                Toast.makeText(this, "Please enter your message..", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // calling a method to send message
            // to our bot to get response.
            sendMessage(userMsgEdt.text.toString())

            // below line we are setting text in our edit text as empty
            userMsgEdt.setText("")
        }

        // on below line we are initializing our adapter class and passing our array list to it.
        messageRVAdapter = MessageRVAdapter(messageModalArrayList, this)

        // below line we are creating a variable for our linear layout manager.
        val linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.layoutManager = linearLayoutManager

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.adapter = messageRVAdapter
    }

    private fun sendMessage(userMsg: String) {
        // below line is to pass message to our
        // array list which is entered by the user.
        messageModalArrayList.add(MessageModal(userMsg, USER_KEY))
        messageRVAdapter.notifyDataSetChanged()

        // url for our brain
        // make sure to add mshape for uid.
        // make sure to add your url.
        val url = "\n" +
                "http://api.brainshop.ai/get?bid=175091&key=ob9hKaHS5pthX8Z3&uid=[uid]&msg=[$userMsg]"

        // creating a variable for our request queue.
        val queue = Volley.newRequestQueue(this)

        // on below line we are making a json object request for a get request and passing our url .
        val jsonObjectRequest =
            JsonObjectRequest(Request.Method.GET, url, null, { response: JSONObject ->
                try {
                    val botResponse = response.getString("cnt")
                    messageModalArrayList.add(MessageModal(botResponse, BOT_KEY))
                    messageRVAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                    messageModalArrayList.add(MessageModal("No response", BOT_KEY))
                    messageRVAdapter.notifyDataSetChanged()
                }
            },
                { error ->
                    messageModalArrayList.add(MessageModal("Sorry no response found", BOT_KEY))
                    Toast.makeText(this, "No response from the bot..", Toast.LENGTH_SHORT).show()
                })

        queue.add(jsonObjectRequest)
    }
}





