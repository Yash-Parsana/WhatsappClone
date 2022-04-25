package com.parsanatech.whatsapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.parsanatech.whatsapp.Models.User
import com.squareup.picasso.Picasso


class userAdapter(lst:ArrayList<User>):RecyclerView.Adapter<viewholder>() {

    val list=lst
    var context:Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val xmltoview=LayoutInflater.from(parent.context).inflate(R.layout.oneuser,parent,false)
        context=parent.context
        return viewholder(xmltoview)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        val curruser=list[position]
        Picasso.get().load(curruser.profilepic.toString()).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.image)
        holder.username.text=curruser.userName

        val senderChat=FirebaseAuth.getInstance().uid+curruser.userId

        FirebaseDatabase.getInstance().reference.child("chats").child(senderChat).orderByChild("time").addListenerForSingleValueEvent(object:ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.hasChildren())
                {
                    snapshot.children.forEach {
                        holder.lastmessage.text=it.child("message").getValue().toString()
                    }
                }
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        holder.itemView.setOnClickListener {

            val intent= Intent(context,ChatDetailsActivity::class.java)
            intent.putExtra("userId",curruser.userId)
            intent.putExtra("profilePic",curruser.profilepic)
            intent.putExtra("userName",curruser.userName)
            context!!.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

}
class viewholder(xmlview: View):RecyclerView.ViewHolder(xmlview){

    val image=xmlview.findViewById<ImageView>(R.id.profile_image)
    val username:TextView=xmlview.findViewById(R.id.username)
    val lastmessage:TextView=xmlview.findViewById(R.id.lastmessage)

}