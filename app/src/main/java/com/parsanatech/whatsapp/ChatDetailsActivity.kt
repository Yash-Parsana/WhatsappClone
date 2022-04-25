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
import com.google.firebase.database.ktx.getValue
import com.parsanatech.whatsapp.Adapter.chatAdapter
import com.parsanatech.whatsapp.Models.User
import com.parsanatech.whatsapp.Models.messageModel
import com.parsanatech.whatsapp.databinding.ActivityChatDetailsBinding
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ChatDetailsActivity : AppCompatActivity() {

    lateinit var binding:ActivityChatDetailsBinding
    var database:FirebaseDatabase?=null
    var auth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityChatDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        database= FirebaseDatabase.getInstance()
        auth= FirebaseAuth.getInstance()

        val senderid:String=auth!!.uid.toString()
        val receverid:String=intent.getStringExtra("userId").toString()
        val username:String=intent.getStringExtra("userName").toString()
        val userpic:String=intent.getStringExtra("profilePic").toString()

        binding.username.text=username
        Picasso.get().load(userpic).placeholder(R.drawable.ic_baseline_account_circle_24).into(binding.profileImage)

        binding.backButton.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }


        val messagelist=ArrayList<messageModel>()
        val chatadapter=chatAdapter(messagelist,this,receverid)

        binding.chatrecyclerview.adapter=chatadapter

        binding.chatrecyclerview.layoutManager=LinearLayoutManager(this)


        val senderChat=senderid+receverid
        val receiverChat=receverid+senderid

        database!!.reference.child("chats").child(senderChat).addValueEventListener(object:
            ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                messagelist.clear()
                snapshot.children.forEach {
                    val Smessage: messageModel = it.getValue(messageModel::class.java) as messageModel

                    Smessage.messageId=it.key.toString()

                    messagelist.add(Smessage)

                }
                chatadapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.sendbtn.setOnClickListener {
            val message= binding.message.text.toString()
            if(message.isNotEmpty()) {
                binding.message.setText("")
                val finammessage = messageModel(senderid, message)

                finammessage.time = Date().time

                database!!.reference.child("chats").child(senderChat).push().setValue(finammessage)
                    .addOnSuccessListener {
                        database!!.reference.child("chats").child(receiverChat).push()
                            .setValue(finammessage).addOnSuccessListener {

                        }
                    }
            }

        }


    }
}