package com.example.messenger.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.messenger.ChattingRoomActivity
import com.example.messenger.Adapter.ChatListItem
import com.example.messenger.R
import com.google.firebase.firestore.FirebaseFirestore
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment  : Fragment(){
    private val TAG = ChatFragment::class.java.simpleName
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //RecyclerView를 초기화 하는 Function Call
        initRecyclerView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_chat, container, false)

        return view
    }

    fun initRecyclerView(){
        val adapter = GroupAdapter<GroupieViewHolder>()

        //firebase의 DB에서 collectionPath가 users이고 필드가 username인 Data를 RecyclerView로 출력
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //username을 받아서 adapter에 push
                    adapter.add(
                        ChatListItem(
                            document.get("username").toString(),
                            document.get("uid").toString()
                        )
                    )
                }
                //recyclerview(chat_list)에 adapter input
                chat_list.adapter = adapter
            }
            .addOnFailureListener { exception ->
                //Firebase와의 연결에 실패함.
                Log.w(TAG, "Error getting documents.", exception)
            }
    
        //adapter를 클릭하면 해당 ChatRoom으로 입장
        //Parameter 2개 -> Expected 2 parameters of types Item<(raw) GroupieViewHolder!>, View
        adapter.setOnItemClickListener{ item, view ->

            //item과 uid 값을 받아옴
            val name : String = (item as ChatListItem).name
            val uid : String = (item as ChatListItem).uid

            //Fragment에서 intent할 때 ".context" keyword 붙여서 보내기
            val intent = Intent(this@ChatFragment.context, ChattingRoomActivity::class.java)

            //adapter(ChatRoom)의 상대 name(email)과 uid를 ChatRoomActivity로 넘겨줌
            intent.putExtra("othersName", name)
            intent.putExtra("othersUid", uid)
            
            startActivity(intent)
        }
    }
}