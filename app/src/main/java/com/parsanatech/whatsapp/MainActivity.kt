package com.parsanatech.whatsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.parsanatech.whatsapp.Adapter.FragmentAdapter
import com.parsanatech.whatsapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    var auth: FirebaseAuth?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.viewpager.adapter=FragmentAdapter(supportFragmentManager)
        binding.tablayout.setupWithViewPager(binding.viewpager)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater:MenuInflater= MenuInflater(this)

        inflater.inflate(R.menu.menu,menu)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId)
        {
            R.id.settings->{
                val intent=Intent(this,settingsActivity::class.java)
                startActivity(intent)
            }
            R.id.logout->{
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this,signin::class.java)
                startActivity(intent)
            }
            R.id.NewGroup->{
                val intent=Intent(this,GroupChatActivity::class.java)
                startActivity(intent)
            }

        }


        return super.onOptionsItemSelected(item)
    }

}