package com.example.messenger.Adapter

import com.example.messenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_contents_others.view.*

class ChatContentsOthers(val msg : String) : Item<GroupieViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_contents_others
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.others_contents.text = msg
    }
}