package com.mahlultech.footballclubs3.favorite

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.FavoriteTeamAdapter
import com.mahlultech.footballclubs3.dbHelper.FavoriteTeam
import com.mahlultech.footballclubs3.dbHelper.database
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class FavoriteTeamsFragment: Fragment(), AnkoComponent<Context>{
    private var favoriteTeams: MutableList<FavoriteTeam> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var adapter: FavoriteTeamAdapter
    private lateinit var progressBar: ProgressBar

    companion object {
        fun newInstance(): FavoriteTeamsFragment {
            val fragment = FavoriteTeamsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progressBar.visible()
        adapter = FavoriteTeamAdapter(ctx, favoriteTeams)
        recyclerView.adapter = adapter
        showFavorite()

        swipeRefresh.onRefresh {
            progressBar.visible()
            favoriteTeams.clear()
            showFavorite()
        }
    }

    override fun createView(ui: AnkoContext<Context>): View {
        return with(ui){
            linearLayout {
                lparams(width = matchParent, height = matchParent)
                orientation = LinearLayout.VERTICAL

                swipeRefresh = swipeRefreshLayout {

                    relativeLayout{
                        lparams (width = matchParent, height = wrapContent)

                        recyclerView = recyclerView {
                            id = R.id.recyclerView
                            layoutManager = LinearLayoutManager(ctx)
                        }.lparams (width = matchParent, height = wrapContent)

                        progressBar = progressBar {
                        }.lparams {
                            centerHorizontally()
                        }
                    }
                }
            }
        }
    }

    private fun showFavorite(){
        context?.database?.use {
            swipeRefresh.isRefreshing = false
            val result = select(FavoriteTeam.TABLE_TEAM)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            favoriteTeams.addAll(favorite)
            adapter.notifyDataSetChanged()
        }
        progressBar.invisible()
    }
}