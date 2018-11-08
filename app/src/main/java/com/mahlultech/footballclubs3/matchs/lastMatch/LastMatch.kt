package com.mahlultech.footballclubs3.matchs.lastMatch

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.model.MatchResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface LastMatchView{
    fun showLoading()
    fun hideLoading()
    fun showLastMatch(data: List<Match>)
}

class LastMatchPresenter(private val view: LastMatchView,
                         private val apiRepository: ApiRepository,
                         private val gson: Gson,
                         private val context: CoroutineContextProvider = CoroutineContextProvider()){

    fun getLastMatch(idLeague: String?){
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getLastMatch(idLeague)),
                        MatchResponse::class.java)
            }
            view.showLastMatch(data.await().events)
            view.hideLoading()
        }
    }

}