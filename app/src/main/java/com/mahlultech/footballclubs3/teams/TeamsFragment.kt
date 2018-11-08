package com.mahlultech.footballclubs3.teams

import android.app.SearchManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import android.widget.*
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.TeamsAdapter
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.League
import com.mahlultech.footballclubs3.model.LeagueResponse
import com.mahlultech.footballclubs3.model.Teams
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.visible
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.coroutines.onClose
import org.jetbrains.anko.appcompat.v7.coroutines.onQueryTextListener
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.design.appBarLayout
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onQueryTextListener
import org.jetbrains.anko.support.v4.*

class TeamsFragment: Fragment(), TeamsView, AnkoComponent<Context> {
    private var teams: MutableList<Teams> = mutableListOf()
    private var leagueArray: ArrayList<String> = ArrayList()
    private var league: List<League> = ArrayList()
    private lateinit var presenter: TeamsPresenter
    private lateinit var adapter: TeamsAdapter
    private lateinit var listTeam: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefresh: SwipeRefreshLayout
    private lateinit var spinner: Spinner
    private lateinit var searchView: SearchView
    private lateinit var leagueName: String

    companion object {
        var LEAGUE_NAME: String = ""
        fun newInstance(): Fragment {
            val teams = TeamsFragment()
            val args = Bundle()
            teams.arguments = args
            return teams
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = TeamsAdapter(ctx, teams)
        listTeam.adapter = adapter

        val request = ApiRepository()
        val gson = Gson()
        presenter = TeamsPresenter(this, request, gson)

        async(UI){
            val data = bg {
                gson.fromJson(request
                        .doRequest(TheSportDBApi.getLeague()),
                        LeagueResponse::class.java)
            }

            league = data.await().leagues
            for (item in league){
                if (item.strSport.equals("Soccer")){
                    leagueArray.add(item.strLeague)
                }
            }

            val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, leagueArray)
            spinner.adapter = spinnerAdapter
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                leagueName = leagueArray[position]
                presenter.getTeamList(leagueName)
                TeamsFragment.LEAGUE_NAME = leagueName
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefresh.onRefresh {
            presenter.getTeamList(leagueName)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showTeamList(data: List<Teams>) {
        swipeRefresh.isRefreshing = false
        teams.clear()
        teams.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun createView(ui: AnkoContext<Context>): View {

        return with(ui) {
            linearLayout {
                lparams (width = matchParent, height = wrapContent)
                orientation = LinearLayout.VERTICAL

                swipeRefresh = swipeRefreshLayout {

                    relativeLayout{
                        lparams (width = matchParent, height = wrapContent)

                        spinner = spinner() {
                            id = R.id.spinner
                            padding = dip(10)
                        }.lparams(width = matchParent, height = wrapContent)

                        listTeam = recyclerView {
                            layoutManager = LinearLayoutManager(ctx)
                        }.lparams (width = matchParent, height = matchParent){
                            below(spinner)
                        }

                        progressBar = progressBar {
                        }.lparams{
                            centerHorizontally()
                        }
                    }
                }
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu_team, menu)

        val searchItem = menu.findItem(R.id.search_team_txt)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Telusuri"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query.isNullOrEmpty()){
                    spinner.visible()
                    presenter.getTeamList(TeamsFragment.LEAGUE_NAME)
                }else {
                    spinner.visibility = View.GONE
                    presenter.searchTeam(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!!.isNotEmpty()) {
                    spinner.visibility = View.GONE
                    presenter.searchTeam(newText)
                } else {
                    spinner.visible()
                    presenter.getTeamList(TeamsFragment.LEAGUE_NAME)
                }
                return false
            }

        })

        searchView.setOnCloseListener {
            presenter.getTeamList(TeamsFragment.LEAGUE_NAME)
            false
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

}