package com.parsanatech.whatsapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.parsanatech.whatsapp.Models.User
import com.parsanatech.whatsapp.databinding.ActivitySignUpBinding
import com.parsanatech.whatsapp.databinding.ActivitySigninBinding

@Suppress("DEPRECATION")
class signin : AppCompatActivity() {

    lateinit var binding:ActivitySigninBinding
    lateinit var progress: android.app.ProgressDialog
    var auth:FirebaseAuth?=null
    var database:FirebaseDatabase?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)


        database= FirebaseDatabase.getInstance()

        progress= ProgressDialog(this)
        progress.setTitle("Login")
        progress.setMessage("Login to your account")



        auth= FirebaseAuth.getInstance()




        binding.signinbtn.setOnClickListener {
            progress.show()

            if(binding.etemail.text.isNotEmpty()&&binding.etpass.text.isNotEmpty())
            {
                auth!!.signInWithEmailAndPassword(binding.etemail.text.toString(),binding.etpass.text.toString()).addOnCompleteListener {task->
                    progress.dismiss()

                    if(task.isSuccessful)
                    {
                        auth=null
                        val intent=Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                    }

                }
            }
            else
            {
                progress.dismiss()

                if(binding.etemail.text.isEmpty())
                    binding.etemail.setError("Please Enter Email")
                if(binding.etpass.text.isEmpty())
                    binding.etemail.setError("Please Enter Password")
            }




        }

        binding.signup.setOnClickListener {
            val intent=Intent(this,sign_up::class.java)
            startActivity(intent)
        }



    }



}