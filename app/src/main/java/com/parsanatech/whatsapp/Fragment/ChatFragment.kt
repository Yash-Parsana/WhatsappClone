package com.parsanatech.whatsapp.Fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.oAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.parsanatech.whatsapp.Models.User
import com.parsanatech.whatsapp.R
import com.parsanatech.whatsapp.databinding.FragmentChatBinding
import com.parsanatech.whatsapp.userAdapter


class ChatFragment : Fragment() {


    var list=ArrayList<User>()
    var database:FirebaseDatabase?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding= FragmentChatBinding.inflate(layoutInflater,container,false)

        val adapter=userAdapter(list)

        binding.chatrecyclerview.adapter=adapter

        val layoutmanager=LinearLayoutManager(context)

        binding.chatrecyclerview.layoutManager=layoutmanager

        database= FirebaseDatabase.getInstance("https://whatsapp-6e939-default-rtdb.firebaseio.com/")

        Log.d("Above Firebase listner","Yes")

        database!!.reference.child("Users").addValueEventListener(object :ValueEventListener{


            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("usersGot",snapshot.children.toString())

                list.clear()
                snapshot.children.forEach{

                    val users: User = it.getValue(User::class.java) as User

                    users.userId=it.key.toString()
                    if(users.userId!= FirebaseAuth.getInstance().uid)
                        list.add(users)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Requestfailed",error.message.toString())
            }

        })

        return  binding.root


    }

}