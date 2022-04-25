package com.parsanatech.whatsapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.parsanatech.whatsapp.Models.User
import com.parsanatech.whatsapp.databinding.ActivitySettingsBinding
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class settingsActivity : AppCompatActivity() {

    lateinit var binding:ActivitySettingsBinding
    lateinit var cloudStorage:FirebaseStorage
    lateinit var auth:FirebaseAuth
    lateinit var database:FirebaseDatabase
    lateinit var progress: android.app.ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()


        auth= FirebaseAuth.getInstance()
        cloudStorage= FirebaseStorage.getInstance()
        database= FirebaseDatabase.getInstance()


        binding.backButton.setOnClickListener {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

        database.reference.child("Users").child(FirebaseAuth.getInstance().uid.toString()).addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user:User=snapshot.getValue(User::class.java) as User

                Picasso.get().load(user.profilepic.toString()).placeholder(R.drawable.ic_baseline_account_circle_24).into(binding.profileImage)

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        binding.setimg.setOnClickListener {

            val intent=Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.type="image/*"
            this.startActivityForResult(intent,10)
        }

//  use name and about
        database.reference.child("Users").child(auth.uid!!).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val user=snapshot.getValue(User::class.java)as User
                binding.username.setText(user.userName.toString())

                if(snapshot.child("about").value.toString()!=null)
                    binding.about.setText(snapshot.child("about").value.toString())

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })

        binding.savebtn.setOnClickListener {

            progress= ProgressDialog(this)
            progress.setTitle("Updating...")
            progress.setMessage("User Name and About...")

            progress.show()

            val newname=binding.username.text.toString()
            val about=binding.about.text.toString()

            val obj=HashMap<String,String>()
            obj.put("userName",newname)
            obj.put("about",about)

            database.reference.child("Users").child(auth.uid!!).updateChildren(obj as Map<String, Any>).addOnCompleteListener {
                progress.dismiss()
                Toast.makeText(this,"Data Updated",Toast.LENGTH_LONG).show()
            }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(data!!.data!=null)
        {
            progress= ProgressDialog(this)
            progress.setTitle("Loading...")
            progress.setMessage("Saving Image...")

            progress.show()
            val file:Uri = data.data!!
            binding.profileImage.setImageURI(file)

            val reference:StorageReference=cloudStorage.reference.child("ProfilePic").child(auth.uid!!)

            reference.putFile(file).addOnSuccessListener {

                reference.downloadUrl.addOnSuccessListener {
                    progress.dismiss()
                    database.reference.child("Users").child(auth.uid!!).child("profilepic").setValue(it!!.toString()).addOnCompleteListener {
                        Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show()

                    }.addOnFailureListener {
                        Log.d("picException",it.toString())
                    }
                }

            }

        }

    }

}