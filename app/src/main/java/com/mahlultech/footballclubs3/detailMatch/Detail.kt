package com.mahlultech.footballclubs3.detailMatch

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.*
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

interface MatchDetailView {
    fun showLoading()
    fun hideLoading()
    fun showTeamHome(data: List<HomeTeam>)
    fun showTeamAway(data: List<AwayTeam>)
    fun showDetailMatch(data: List<Match>)
}

class MatchDetailPresenter(private val view: MatchDetailView,
                          private val apiRepository: ApiRepository,
                          private val gson: Gson,
                          private val context: CoroutineContextProvider = CoroutineContextProvider()){

    fun getTeamHome(idTeam: String?) {
        view.showLoading()
        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getHomeTeam(idTeam)),
                        HomeTeamResponse::class.java)
            }

            view.showTeamHome(data.await().teams)
            view.hideLoading()
        }
    }

    fun getTeamAway(idTeam: String?) {
        view.showLoading()
        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getAwayTeam(idTeam)),
                        AwayTeamResponse::class.java)
            }

            view.showTeamAway(data.await().teams)
            view.hideLoading()
        }
    }

    fun getMatchDetail(idEvent: String?) {
        view.showLoading()
        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getDetailMatch(idEvent)),
                        MatchResponse::class.java)
            }
            view.showDetailMatch(data.await().events)
            view.hideLoading()
        }
    }

}