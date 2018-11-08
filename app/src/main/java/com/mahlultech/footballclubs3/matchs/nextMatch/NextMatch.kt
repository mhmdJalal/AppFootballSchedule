package com.mahlultech.footballclubs3.matchs.nextMatch

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.League
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.model.MatchResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface NextMatchView{
    fun showLoading()
    fun hideLoading()
    fun showNextMatch(data: List<Match>)
}

class NextMatchPresenter(private val view: NextMatchView,
                         private val apiRepository: ApiRepository,
                         private val gson: Gson,
                         private val context: CoroutineContextProvider = CoroutineContextProvider()){

    fun getNextMatch(idLeague: String?){
        view.showLoading()

        async(context.main) {
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getNextMatch(idLeague)),
                        MatchResponse::class.java)
            }
            view.showNextMatch(data.await().events)
            view.hideLoading()
        }
    }

}