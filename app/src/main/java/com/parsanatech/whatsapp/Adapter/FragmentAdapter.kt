package com.parsanatech.whatsapp.Adapter

import android.os.Parcel
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.parsanatech.whatsapp.Fragment.ChatFragment
import com.parsanatech.whatsapp.Fragment.callFragment
import com.parsanatech.whatsapp.Fragment.statusFragment

class FragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return  3
    }

    override fun getItem(position: Int): Fragment {

        when(position)
        {
            0-> return ChatFragment()
            1-> return statusFragment()
            2-> return callFragment()
            else -> return ChatFragment()

        }

    }

    override fun getPageTitle(position: Int): CharSequence? {

        var title:String=""
        when(position)
        {
            0-> title="CHATS"
            1-> title="STATUS"
            2-> title="CALLS"
        }

        return title
    }

}