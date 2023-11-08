package com.example.chatequipefrance.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatequipefrance.R
import com.example.chatequipefrance.activities.ChatActivity
import com.example.chatequipefrance.models.User

class UsersRecyclerAdapter : RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>(), Filterable {

    var items: MutableList<User> = mutableListOf()
        set(value) {
            field = value
            usersFilterdList = value
            notifyDataSetChanged()
        }

    private var usersFilterdList: MutableList<User> = mutableListOf()

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSequence = constraint.toString()
                if (charSequence.isEmpty()) {
                    usersFilterdList = items
                } else {
                    val resultList =
                        items.filter { it.fullname.lowercase().contains(charSequence.lowercase()) }
                    usersFilterdList = resultList as MutableList<User>
                }
                val filterResults = FilterResults()
                filterResults.values = usersFilterdList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFilterdList = results?.values as MutableList<User>
                notifyDataSetChanged()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = usersFilterdList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = usersFilterdList[position]
        holder.bin(user)
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvShortName: TextView = itemView.findViewById(R.id.tvShortName)
        val tvName: TextView = itemView.findViewById(R.id.tvName)

        fun bin(user: User) {
            tvShortName.text = user.fullname[0].toString()
            tvName.text = user.fullname

            itemView.setOnClickListener {
                Intent(itemView.context, ChatActivity::class.java).also {
                    it.putExtra("friend", user.uuid)
                    itemView.context.startActivity(it)
                }
            }

        }
    }


}