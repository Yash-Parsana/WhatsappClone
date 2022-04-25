package com.parsanatech.whatsapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.parsanatech.whatsapp.Models.messageModel
import com.parsanatech.whatsapp.R


@Suppress("UNREACHABLE_CODE")
class chatAdapter : RecyclerView.Adapter<viewholders> {



    var messageLst: ArrayList<messageModel>
    var contaxt: Context
    var senderId:String?=null
    var receiverId:String?=null

    val SENDER_VIEW_TYPE: Int = 1
    val RECEIVER_VIEW_TYPE: Int = 2

    constructor(messageLst: ArrayList<messageModel>, contaxt: Context) : super() {
        this.messageLst = messageLst
        this.contaxt = contaxt
    }

    constructor(
        messageLst: ArrayList<messageModel>,
        contaxt: Context,
        receiverId: String?
    ) : super() {
        this.messageLst = messageLst
        this.contaxt = contaxt
        this.receiverId = receiverId
    }




    override fun getItemViewType(position: Int): Int {


        if (messageLst.get(position).id == FirebaseAuth.getInstance().uid) {
            return SENDER_VIEW_TYPE

        } else return RECEIVER_VIEW_TYPE


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholders {
        if (viewType == SENDER_VIEW_TYPE) {
            val xmltoview =
                LayoutInflater.from(contaxt).inflate(R.layout.sender_message, parent, false)
            return viewholders.senderViewholder(xmltoview)
        }
        val xmltoview =
            LayoutInflater.from(contaxt).inflate(R.layout.receiver_message, parent, false)
        return viewholders.receiverViewholder(xmltoview)
    }

    override fun onBindViewHolder(holder: viewholders, position: Int) {

        val currMessage = messageLst.get(position)

        senderId=FirebaseAuth.getInstance().uid.toString()

        val senderChat=senderId+receiverId

        holder.itemView.setOnLongClickListener{

            AlertDialog.Builder(contaxt).setTitle("Delete").setMessage("Are you sure you want to delete message")
                .setPositiveButton("yes"){dialogInterface, which ->

                    FirebaseDatabase.getInstance().reference.child("chats")
                        .child(senderChat).child(currMessage.messageId.toString()).setValue(null)

                    FirebaseDatabase.getInstance().reference.child("GroupChat").child(currMessage.messageId.toString()).setValue(null)


                }.setNegativeButton("No"){dialogInterface, which ->

                }.show()

            return@setOnLongClickListener true
        }


        if (holder.itemViewType == SENDER_VIEW_TYPE) {
            (holder as viewholders.senderViewholder).senderMessage.text =
                currMessage.message.toString()
        } else {
            (holder as viewholders.receiverViewholder).receiverMessage.text =
                currMessage.message.toString()
        }



    }

    override fun getItemCount(): Int {
        return messageLst.size
    }


}



sealed class viewholders(xmlview: View) : RecyclerView.ViewHolder(xmlview) {

    class receiverViewholder(myview: View) : viewholders(myview) {
        val receiverMessage = myview.findViewById<TextView>(R.id.receivermessage)
        val receiverMessageTime = myview.findViewById<TextView>(R.id.receivermessagetime)

    }

    class senderViewholder(myview: View) : viewholders(myview) {
        val senderMessageTime = myview.findViewById<TextView>(R.id.sendermessagetime)
        val senderMessage = myview.findViewById<TextView>(R.id.sendermessage)
    }


}


