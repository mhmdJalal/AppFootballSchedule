package com.mahlultech.footballclubs3.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.mahlultech.footballclubs3.favorite.FavoriteMatchsFragment
import com.mahlultech.footballclubs3.favorite.FavoriteTeamsFragment

class FavoritePagerAdapter(fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> FavoriteMatchsFragment()
            1 -> FavoriteTeamsFragment()
            else -> FavoriteMatchsFragment()
        }
    }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Matches"
            1 -> "Teams"
            else -> "Matches"
        }
    }
}