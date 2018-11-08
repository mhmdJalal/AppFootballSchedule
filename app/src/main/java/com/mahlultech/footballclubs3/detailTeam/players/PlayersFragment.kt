package com.mahlultech.footballclubs3.detailTeam.players

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.PlayerAdapter
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.detailTeam.overview.OverviewFragment
import com.mahlultech.footballclubs3.model.Player
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class PlayersFragment: Fragment(), AnkoComponent<Context>, PlayerView {
    private var players: MutableList<Player> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var presenter: PlayerPresenter
    private lateinit var adapter: PlayerAdapter

    companion object {
        fun newInstance(idTeam: String): PlayersFragment {
            val fragment = PlayersFragment()
            val args = Bundle()
            args.putString("ID_TEAM", idTeam)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val idTeam = arguments?.getString("ID_TEAM")

        adapter = PlayerAdapter(ctx, players)
        recyclerView.adapter = adapter

        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = PlayerPresenter(this, apiRepository, gson)
        presenter.getPlayerList(idTeam)

        swipeRefreshLayout.onRefresh {
            presenter.getPlayerList(idTeam)
        }
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        relativeLayout {
            lparams(width = matchParent, height = matchParent)

            swipeRefreshLayout = swipeRefreshLayout {

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

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showPlayerList(data: List<Player>) {
        swipeRefreshLayout.isRefreshing = false
        players.clear()
        players.addAll(data)
        adapter.notifyDataSetChanged()
    }

}