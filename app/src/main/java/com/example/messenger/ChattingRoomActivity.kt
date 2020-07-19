package com.example.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.messenger.navigation.ChatFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chatting_room.*

class ChattingRoomActivity : AppCompatActivity() {

    private val TAG = ChatFragment::class.java.simpleName
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_room)

        ChatMessage()
    }

    fun ChatMessage(){
        val adapter = GroupAdapter<GroupieViewHolder>()
        
        //RecyclerView에 Adapter연결
        message_contents.adapter = adapter


    }
}