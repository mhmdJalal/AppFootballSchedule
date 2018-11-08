package com.mahlultech.footballclubs3.matchs.lastMatch

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.MatchAdapter
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.League
import com.mahlultech.footballclubs3.model.LeagueResponse
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.visible
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class LastMatchFragment: Fragment(), LastMatchView, AnkoComponent<Context> {

    companion object {
        var itemSelected: String = ""
    }

    private var matchs: MutableList<Match> = mutableListOf()

    private var leagueArray: ArrayList<String> = ArrayList()
    private var idLeagueArray: ArrayList<String> = ArrayList()
    private lateinit var league: List<League>

    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: MatchAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: LastMatchPresenter
    private lateinit var spinner: Spinner
    private var idLeague: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = MatchAdapter(ctx, matchs)
        recyclerView.adapter = adapter

        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = LastMatchPresenter(this, apiRepository, gson)

        async(UI){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getLeague()),
                        LeagueResponse::class.java)
            }

            league = data.await().leagues
            for (item in league){
                if (item.strSport.equals("Soccer")){
                    leagueArray.add(item.strLeague)
                    idLeagueArray.add(item.idLeague)
                }
            }

            val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, leagueArray)
            spinner.adapter = spinnerAdapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                idLeague = idLeagueArray[position]
                presenter.getLastMatch(idLeague)
                itemSelected = idLeague.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefreshLayout.onRefresh {
            presenter.getLastMatch(idLeague)
        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLastMatch(data: List<Match>) {
        swipeRefreshLayout.isRefreshing = false
        matchs.clear()
        matchs.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun createView(ui: AnkoContext<Context>): View {
        return with(ui){
            linearLayout {
                lparams(width = matchParent, height = matchParent)
                orientation = LinearLayout.VERTICAL

                swipeRefreshLayout = swipeRefreshLayout {

                    relativeLayout{
                        lparams (width = matchParent, height = wrapContent)

                        spinner = spinner(){
                            id = R.id.spinner
                            padding = dip(10)
                        }.lparams(width = matchParent, height = wrapContent)

                        recyclerView = recyclerView {
                            id = R.id.recyclerViewLast
                            layoutManager = LinearLayoutManager(ctx)
                        }.lparams (width = matchParent, height = matchParent){
                            below(spinner)
                        }

                        progressBar = progressBar {
                        }.lparams {
                            centerHorizontally()
                        }
                    }
                }
            }
        }
    }
}

