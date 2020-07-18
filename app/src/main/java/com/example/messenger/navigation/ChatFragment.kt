package com.example.messenger.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.messenger.Model.ChatListItem
import com.example.messenger.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment  : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var adapter = GroupAdapter<GroupieViewHolder>()

        for(i in 0 .. 2) {
            adapter.add(ChatListItem())
        }

        chat_list.adapter = adapter

        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_chat, container, false)

        return view
    }
}