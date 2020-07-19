package com.example.messenger.Adapter

import com.example.messenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatContentsMine : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_contents_mine
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
}