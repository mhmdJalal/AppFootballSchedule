package com.mahlultech.footballclubs3.matchs

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.*
import android.widget.LinearLayout
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.search.SearchMatchActivity
import com.mahlultech.footballclubs3.adapter.MatchPagerAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.design.tabLayout
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.viewPager
import org.jetbrains.anko.design.themedAppBarLayout

class MatchFragment: Fragment(), AnkoComponent<Context> {
    private lateinit var tablayout: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var pagerAdapter: MatchPagerAdapter

    companion object {
        fun newInstance(): Fragment {
            val teams = MatchFragment()
            val args = Bundle()
            teams.arguments = args
            return teams
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v: View =  createView(AnkoContext.create(ctx))
        viewPager = v.findViewById(R.id.viewPager)
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        pagerAdapter = MatchPagerAdapter(fragmentManager)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            R.id.searchMenu -> ctx.startActivity<SearchMatchActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

}