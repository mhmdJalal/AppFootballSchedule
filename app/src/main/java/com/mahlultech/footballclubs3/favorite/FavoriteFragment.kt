package com.mahlultech.footballclubs3.favorite

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.FavoritePagerAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.design.themedAppBarLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager

class FavoriteFragment: Fragment(), AnkoComponent<Context> {
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: FavoritePagerAdapter

    companion object {
        fun newInstance(): Fragment {
            val fragment = FavoriteFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View =  createView(AnkoContext.create(ctx))
        viewPager = v.findViewById(R.id.viewPager)
        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        pagerAdapter = FavoritePagerAdapter(fragmentManager)
        viewPager.adapter = pagerAdapter

        tablayout.setupWithViewPager(viewPager)
    }

    override fun createView(ui: AnkoContext<Context>): View  = with(ui){

        linearLayout {
            lparams(width = matchParent, height = matchParent)
            orientation = LinearLayout.VERTICAL

            themedAppBarLayout(R.style.AppTheme) {

                tablayout = tabLayout {
                    tabTextColors = ColorStateList.valueOf(Color.WHITE)
                    setSelectedTabIndicatorColor(Color.WHITE)
                }.lparams(width = matchParent, height = wrapContent)

            }

            viewPager = viewPager {
                id = R.id.viewPager
            }.lparams(width = matchParent, height = matchParent)
        }

    }
}