package com.mahlultech.footballclubs3.detailTeam.overview

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.Teams
import com.mahlultech.footballclubs3.model.TeamsResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface Overview{
    fun showLoading()
    fun hideLoading()
    fun showOverview(data: List<Teams>)
}

class OverviewPresenter(val view: Overview,
                        val apiRepository: ApiRepository,
                        val gson: Gson,
                        val context: CoroutineContextProvider = CoroutineContextProvider()) {
    fun getTeamDetail(idTeams: String?){
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getDetailTeam(idTeams)),
                        TeamsResponse::class.java)
            }

            view.showOverview(data.await().teams)
            view.hideLoading()
        }
    }
}