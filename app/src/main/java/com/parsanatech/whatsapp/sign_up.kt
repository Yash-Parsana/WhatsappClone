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
import com.parsanatech.whatsapp.databinding.ActivityMainBinding
import com.parsanatech.whatsapp.databinding.ActivitySignUpBinding

@Suppress("DEPRECATION")
class sign_up : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    var auth: FirebaseAuth?=null
    var googleSignInClient: GoogleSignInClient? =null
    lateinit var database:FirebaseDatabase
    lateinit var progress: android.app.ProgressDialog
    lateinit var progress1: android.app.ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        progress= ProgressDialog(this)
        progress.setTitle("Creating Account")
        progress.setMessage("We are Creating your account")


        progress1= ProgressDialog(this)
        progress1.setTitle("Login")
        progress1.setMessage("Loading...")

        auth= FirebaseAuth.getInstance()

        database= FirebaseDatabase.getInstance("https://whatsapp-6e939-default-rtdb.firebaseio.com/")



        // google signin process
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)

        //google btn
        binding.btngoogle.setOnClickListener {
            progress1.show()
            signIn()
        }

        binding.signupbtn.setOnClickListener {

            progress.show()

            if(binding.etemail.text.isNotEmpty()&&binding.etpass.text.isNotEmpty()&&binding.etuesername.text.isNotEmpty())
            {
                auth!!.createUserWithEmailAndPassword(binding.etemail.text.toString(),binding.etpass.text.toString()).addOnCompleteListener {task->

                    progress.dismiss()

                    if(task.isSuccessful)
                    {
                        val user=User(binding.etuesername.text.toString(),binding.etemail.text.toString(),binding.etpass.text.toString())

                        val id=task.result!!.user!!.uid

                        database.reference.child("Users").child(id).setValue(user)

                        Toast.makeText(this,"User Created",Toast.LENGTH_LONG).show()
                    }
                    else{
                        Toast.makeText(this,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                    }

                }

            }
            else
            {
                progress.dismiss()

                if(binding.etemail.text.isEmpty())
                    binding.etemail.setError("Please Enter Your Email")
                if(binding.etuesername.text.isEmpty())
                    binding.etuesername.setError("Plaese Enter Your Name")
                if(binding.etpass.text.isEmpty())
                    binding.etpass.setError("Please Enter Your Password")

            }

        }

        binding.signin.setOnClickListener {
            val intent=Intent(this,signin::class.java)
            startActivity(intent)
            finish()
        }

        if(auth?.currentUser!=null)
        {
            Log.d("yashparsana",auth!!.currentUser.toString())
            intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    // google signin process
    val RC_SIGN_IN=8
    private fun signIn() {
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                progress1.dismiss()
                Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show()
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth!!.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                progress1.dismiss()
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
//                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth?.currentUser

                    var users=User()

                    users.userId=user?.uid
                    users.userName=user?.displayName
                    users.profilepic=user?.photoUrl.toString()

                    database!!.reference.child("Users").child(user!!.uid).setValue(users)

                    val intent=Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(this,task.exception?.message.toString(),Toast.LENGTH_LONG).show()
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }
}