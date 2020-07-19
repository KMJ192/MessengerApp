package com.example.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.messenger.Adapter.ChatContentsMine
import com.example.messenger.Adapter.ChatContentsOthers
import com.example.messenger.Model.ChatModel
import com.example.messenger.navigation.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chatting_room.*

class ChattingRoomActivity : AppCompatActivity() {

    private val TAG = ChatFragment::class.java.simpleName
    val db = FirebaseFirestore.getInstance()

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatting_room)

        //Message를 DB로 Input, Output하는 Function
        ChatMessage(db)

    }

    fun ChatMessage(db : FirebaseFirestore){
        val adapter = GroupAdapter<GroupieViewHolder>()

        auth = FirebaseAuth.getInstance()

        val myUid = auth.uid
        val othersName = intent.getStringExtra("othersName")
        val othersUid = intent.getStringExtra("othersUid")

        //RecyclerView에 Adapter연결
        message_contents.adapter = adapter

        //DB의 내용 Output
        db.collection("message")
            .get()
            .addOnSuccessListener {result ->
                //Firebase에 있는 내용 Loop
                for(document in result){
                    val senderUid = document.get("myUid")
                    val msg = document.get("msg")

                    //채팅방의 내용을 출력함
                    if(senderUid == myUid){
                        //DB의 Uid가 접속중인 User의 Uid와 일치
                        adapter.add(ChatContentsMine())
                    }else{
                        //DB의 Uid가 접속중인 User의 Uid와 비일치
                        adapter.add(ChatContentsOthers())
                    }
                }
            }
            .addOnFailureListener{

            }

        button_inputContent.setOnClickListener{
            val message = edit_text_chat.text.toString()
            edit_text_chat.setText("") //textbox 내용 제거

            //DB에 message 내용 push
            val chat = ChatModel(myUid, othersName, othersUid)
            db.collection("message")
                .add(chat)
                .addOnSuccessListener {
                    //Success input message to DB
                    Log.d(TAG, "message input Success")
                }
                .addOnFailureListener{
                    //Failed input message to DB
                    Log.d(TAG, "message input Failed")
                }

        }
    }
}