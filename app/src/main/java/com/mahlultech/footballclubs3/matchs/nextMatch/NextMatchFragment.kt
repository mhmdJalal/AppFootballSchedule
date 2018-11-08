package com.mahlultech.footballclubs3.matchs.nextMatch

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.NextMatchAdapter
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.detailMatch.DetailMatchActivity
import com.mahlultech.footballclubs3.model.League
import com.mahlultech.footballclubs3.model.LeagueResponse
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.timeFormatter
import com.mahlultech.footballclubs3.utils.visible
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.support.v4.swipeRefreshLayout
import java.util.*
import java.util.concurrent.TimeUnit

class NextMatchFragment: Fragment(), NextMatchView, AnkoComponent<Context> {

    private var leagueArray: ArrayList<String> = ArrayList()
    private var idLeagueArray: ArrayList<String> = ArrayList()
    private lateinit var league: List<League>
    private lateinit var spinner: Spinner
    private var idLeague: String? = null

    private var matchs: MutableList<Match> = mutableListOf()
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var adapter: NextMatchAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: NextMatchPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = NextMatchAdapter(matchs){
            startActivity<DetailMatchActivity>(
                    "idEvent" to it.idEvent,
                    "idAway" to it.idAwayTeam,
                    "idHome" to it.idHomeTeam)
        }

        adapter.enableReminder {
            createEventOnCalender(it)
        }

        recyclerView.adapter = adapter

        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = NextMatchPresenter(this, apiRepository, gson)

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
                presenter.getNextMatch(idLeague)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        swipeRefreshLayout.onRefresh {
            presenter.getNextMatch(idLeague)
        }
    }

    private fun createEventOnCalender(match: Match){
        val intent = Intent(Intent.ACTION_INSERT)
        intent.type = "vnd.android.cursor.item/event"

        val year = TextUtils.substring(match.dateEvent, 0, 4)
        val month = TextUtils.substring(match.dateEvent, 5, 7)
        val day = TextUtils.substring(match.dateEvent, 8, 10)
        val hour = TextUtils.substring(match.strTime?.timeFormatter(), 0, 2)
        val minute = TextUtils.substring(match.strTime?.timeFormatter(), 3, 5)

        Log.i("YEAR", year)
        Log.i("MONTH", month)
        Log.i("DAY", day)
        Log.i("HOUR", hour)
        Log.i("MINUTE", minute)

        val time = Calendar.getInstance()
        time.set(year.toInt(), month.toInt() - 1, day.toInt(), hour.toInt(), minute.toInt())
        val startTime = time.timeInMillis
        val endTime = startTime + TimeUnit.MINUTES.toMillis(90)

        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false)
        intent.putExtra(CalendarContract.Events.DURATION, "+P1H")
        intent.putExtra(CalendarContract.Events.HAS_ALARM, 1)

        intent.putExtra(CalendarContract.Events.TITLE, "${match.strHomeTeam} vs ${match.strAwayTeam} ")
        intent.putExtra(CalendarContract.Events.DESCRIPTION, "Reminder for match between ${match.strHomeTeam} and ${match.strAwayTeam}")

        startActivity(intent)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showNextMatch(data: List<Match>) {
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
                            id = R.id.recyclerView
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