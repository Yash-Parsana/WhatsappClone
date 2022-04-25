package com.parsanatech.whatsapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.parsanatech.whatsapp.Adapter.chatAdapter
import com.parsanatech.whatsapp.Models.messageModel
import com.parsanatech.whatsapp.databinding.ActivityGroupChatBinding
import java.util.*
import kotlin.collections.ArrayList

class GroupChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityGroupChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding=ActivityGroupChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


        val database=FirebaseDatabase.getInstance()
        val senderid=FirebaseAuth.getInstance().uid
        val lst=ArrayList<messageModel>()

        val adapter=chatAdapter(lst,this)

        binding.chatrecyclerview.adapter=adapter
        binding.chatrecyclerview.layoutManager=LinearLayoutManager(this)

        binding.username.text="Group Chat"

        database.reference.child("GroupChat").addValueEventListener(object:
            ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                lst.clear()
                snapshot.children.forEach {
                    val messages=it.getValue(messageModel::class.java) as messageModel

                    messages.messageId=it.key.toString()

                    lst.add(messages)
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


        binding.sendbtn.setOnClickListener {

            val message=binding.message.text.toString()

            val finalmessage=messageModel(senderid,message)

            finalmessage.time= Date().time

            database.reference.child("GroupChat").push().setValue(finalmessage).addOnSuccessListener {

            }

        }



    }
}