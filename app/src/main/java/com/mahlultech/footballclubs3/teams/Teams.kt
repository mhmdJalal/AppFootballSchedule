package com.mahlultech.footballclubs3.teams

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.League
import com.mahlultech.footballclubs3.model.LeagueResponse
import com.mahlultech.footballclubs3.model.Teams
import com.mahlultech.footballclubs3.model.TeamsResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface TeamsView{
    fun showLoading()
    fun hideLoading()
    fun showTeamList(data: List<Teams>)
}

class TeamsPresenter(private val view: TeamsView,
                     private val apiRepository: ApiRepository,
                     private val gson: Gson,
                     private val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getTeamList(league: String?) {
        view.showLoading()

        async(context.main) {

            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getTeams(league)),
                        TeamsResponse::class.java)
            }
            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }

    fun searchTeam(team: String?){
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.searchTeam(team)),
                        TeamsResponse::class.java)
            }

            view.showTeamList(data.await().teams)
            view.hideLoading()
        }
    }
}