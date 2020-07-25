package com.example.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.messenger.Adapter.ChatContentsMine
import com.example.messenger.Adapter.ChatContentsOthers
import com.example.messenger.Model.ChatModel
import com.example.messenger.navigation.ChatFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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

        //리얼타임 DB
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")
        val readRef =database.getReference("message").child(myUid.toString()).child(othersUid)

        val childEventListener = object: ChildEventListener{
            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val model = snapshot.getValue(ChatModel::class.java)

                val msg = model?.message.toString()
                val who = model?.who.toString()

                if(who == "mine"){
                    adapter.add(ChatContentsMine(msg))
                }else{
                    adapter.add(ChatContentsOthers(msg))
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

        }

        message_contents.adapter = adapter
        readRef.addChildEventListener(childEventListener)

        //확인 버튼 클릭시 DB로 Message 전송
        button_inputContent.setOnClickListener{
            val message = edit_text_chat.text.toString()
            edit_text_chat.setText("")

            val chat_mine = ChatModel(myUid, othersUid, message, System.currentTimeMillis(), "mine")
            myRef.child(myUid.toString()).child(othersUid).push().setValue(chat_mine)

            val chat_others = ChatModel(othersUid, myUid, message, System.currentTimeMillis(), "others")
            myRef.child(othersUid).child(myUid.toString()).push().setValue(chat_others)
        }
    }
}