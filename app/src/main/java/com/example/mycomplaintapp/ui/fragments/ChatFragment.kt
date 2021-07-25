package com.example.mycomplaintapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycomplaintapp.R
import com.example.mycomplaintapp.databinding.FragmentChatBinding
import com.example.mycomplaintapp.models.ChatModel
import com.example.mycomplaintapp.ui.adapters.ChatAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private val mArgs: ChatFragmentArgs by navArgs()
    private val chatList = ArrayList<ChatModel>()
    private lateinit var adapter: ChatAdapter

    private lateinit var senderId: String
    private lateinit var receiverId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentChatBinding.inflate(inflater, container, false)

        senderId = mArgs.complaint.userId
        receiverId = mArgs.complaint.deptName

        setupRecyclerView()
        readMessages(senderId, receiverId)

        binding.imgSendMessage.setOnClickListener {
            sendMessage(senderId, receiverId)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.chatRecyclerView.also {
            it.layoutManager = LinearLayoutManager(context)
            it.setHasFixedSize(true)
        }
    }

    private fun sendMessage(senderId: String, receiverId: String) {
        if (binding.etTypeMessage.text.toString().isEmpty()) {
            Toast.makeText(context, "Message could not be empty", Toast.LENGTH_SHORT).show()
        } else {
            val hashMap: HashMap<String, String> = HashMap()

            hashMap["senderId"] = senderId
            hashMap["receiverId"] = receiverId
            hashMap["message"] = binding.etTypeMessage.text.toString()

            FirebaseDatabase.getInstance().getReference("Chat")
                .push()
                .setValue(hashMap)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(context, "Message sent successfully", Toast.LENGTH_SHORT)
                            .show()
                        binding.etTypeMessage.setText("")
                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        context,
                        "Message sending error: ${it.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.etTypeMessage.setText("")
                }
        }
    }

    private fun readMessages(senderId: String, receiverId: String) {
        FirebaseDatabase.getInstance().getReference("Chat")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    chatList.clear()

                    for (chatSnapShots in snapshot.children) {

                        val chat = chatSnapShots.getValue(ChatModel::class.java)

                        if (chat!!.senderId == senderId && chat!!.receiverId == receiverId ||
                            chat!!.senderId == receiverId && chat!!.receiverId == senderId
                        ) {

                            chatList.add(chat)

                        }
                    }

                    adapter = ChatAdapter(senderId, chatList)
                    binding.chatRecyclerView.adapter = adapter
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        context,
                        "Loading chat error: ${error.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }
}