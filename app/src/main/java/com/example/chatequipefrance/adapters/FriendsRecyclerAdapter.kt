package com.example.chatequipefrance.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatequipefrance.R
import com.example.chatequipefrance.activities.ChatActivity
import com.example.chatequipefrance.models.Friend
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FriendsRecyclerAdapter : RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder>() {

    var items: MutableList<Friend> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val friend = items[position]
        holder.bin(friend)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivFriend: ShapeableImageView = itemView.findViewById(R.id.ivFriend)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvLastMsg: TextView = itemView.findViewById(R.id.tvLastMsg)
        val tvHour: TextView = itemView.findViewById(R.id.tvHour)

        fun bin(friend: Friend) {
            tvName.text = friend.name
            tvLastMsg.text = friend.lastMsg
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            tvHour.text = sdf.format(Date(friend.timestamp))

            if (friend.image.isNotEmpty()) {
                Glide.with(itemView.context).load(friend.image).placeholder(R.drawable.ic_avatar).into(ivFriend)
            }

            itemView.setOnClickListener {
                Intent(itemView.context, ChatActivity::class.java).also {
                    it.putExtra("friend", friend.uuid)
                    itemView.context.startActivity(it)
                }
            }
        }

    }
}