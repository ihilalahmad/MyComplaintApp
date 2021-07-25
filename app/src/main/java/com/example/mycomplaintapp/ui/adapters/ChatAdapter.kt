package com.example.mycomplaintapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.models.ChatModel


/**
 * Created by Hilal Ahmad
 * on 7/25/21 2:16 PM
 **/

class ChatAdapter(
    private val userId: String,
    private val chatList: ArrayList<ChatModel>
) : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_RECEIVED = 0
    private val MESSAGE_TYPE_SENT = 1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == MESSAGE_TYPE_SENT) {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_sent, parent, false)
            ViewHolder(view)
        }else {
            val view =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
            ViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chatList[position]
        holder.tvMessage.text = chat.message
    }

    override fun getItemCount() = chatList.size

    override fun getItemViewType(position: Int): Int {
        return if (chatList[position].senderId == userId) {
            MESSAGE_TYPE_SENT
        } else {
            MESSAGE_TYPE_RECEIVED
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMessage: TextView = view.findViewById(R.id.tvMessage)
    }
}