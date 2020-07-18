package com.example.messenger.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.messenger.Model.ChatListItem
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
        val adapter = GroupAdapter<GroupieViewHolder>()
        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    adapter.add(ChatListItem(document.get("username").toString()))
                    Log.d(TAG, "username => ${document.get("username").toString()}, ${document.id} => ${document.data}")
                }
                chat_list.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_chat, container, false)

        return view
    }
}