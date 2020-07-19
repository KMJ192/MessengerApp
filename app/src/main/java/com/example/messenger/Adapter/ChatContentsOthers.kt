package com.example.messenger.Adapter

import com.example.messenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatContentsOthers : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_contents_others
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
}