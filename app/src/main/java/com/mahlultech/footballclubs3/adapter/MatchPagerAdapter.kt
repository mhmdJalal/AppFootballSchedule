package com.mahlultech.footballclubs3.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mahlultech.footballclubs3.matchs.lastMatch.LastMatchFragment
import com.mahlultech.footballclubs3.matchs.nextMatch.NextMatchFragment

class MatchPagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> NextMatchFragment()
            1 -> LastMatchFragment()
            else -> NextMatchFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Next Match"
            1 -> "last Match"
            else -> "Next Match"
        }
    }
}