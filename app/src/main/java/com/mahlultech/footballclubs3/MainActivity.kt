package com.mahlultech.footballclubs3

import android.annotation.SuppressLint
import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.SearchView
import com.mahlultech.footballclubs3.favorite.FavoriteFragment
import com.mahlultech.footballclubs3.matchs.MatchFragment
import com.mahlultech.footballclubs3.teams.TeamsFragment
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.design.bottomNavigationView

class MainActivity : AppCompatActivity() {

    @SuppressLint("PrivateResource")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        linearLayout {
            lparams(width = matchParent, height = matchParent)
            orientation = LinearLayout.VERTICAL
            backgroundResource = R.color.background

            frameLayout {
                id = R.id.layout_container
                backgroundResource = R.color.background
            }.lparams {
                width = matchParent
                height = dip(0)
                weight = 1f
            }

            bottomNavigationView {
                id = R.id.bottom_navigation
                inflateMenu(R.menu.main_menu)
                backgroundColor = Color.WHITE

                setOnNavigationItemSelectedListener { item ->

                    val selectedFragment: Fragment?
                    when(item.itemId){
                        R.id.matchs -> {
                            selectedFragment = MatchFragment.newInstance()
                            loadFragment(selectedFragment)
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.teams -> {
                            selectedFragment = TeamsFragment.newInstance()
                            loadFragment(selectedFragment)
                            return@setOnNavigationItemSelectedListener true
                        }
                        R.id.favorite -> {
                            selectedFragment = FavoriteFragment.newInstance()
                            loadFragment(selectedFragment)
                            return@setOnNavigationItemSelectedListener true
                        }
                    }
                    return@setOnNavigationItemSelectedListener false
                }
            }.lparams(width = matchParent, height = dip(80)) {
                gravity = Gravity.BOTTOM
            }
        }

        supportFragmentManager.beginTransaction()
                .add(R.id.layout_container, MatchFragment())
                .commit()
    }

    @SuppressLint("PrivateResource")
    private fun loadFragment(fragment: Fragment = MatchFragment.newInstance()){
        supportFragmentManager.beginTransaction()
                .replace(R.id.layout_container, fragment)
                .commit()
    }

}
