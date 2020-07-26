package com.example.messenger.Adapter

import com.example.messenger.R
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_list_item.view.*

class UserListItem(val name : String, val uid : String) : Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_list_item
    }
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username.text = name
    }
}