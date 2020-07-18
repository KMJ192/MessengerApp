package com.example.messenger.Model

import com.example.messenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatListItem : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_list_item
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }

}