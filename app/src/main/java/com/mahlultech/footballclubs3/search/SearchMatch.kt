package com.mahlultech.footballclubs3.search

import android.util.Log
import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.model.MatchResponse
import com.mahlultech.footballclubs3.model.SearchMatchResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface SearchMatchView {
    fun showLoading()
    fun hideLoading()
    fun showSearchMatch(data: List<Match>)
}

class SearchMatchPresenter (val view: SearchMatchView,
                            val apiRepository: ApiRepository,
                            val gson: Gson,
                            val context: CoroutineContextProvider = CoroutineContextProvider()){
    fun searchMatch(match: String?){

        async(context.main) {
            try {
                view.showLoading()

                val data = bg {
                    gson.fromJson(apiRepository
                            .doRequest(TheSportDBApi.searchMatch(match)),
                            SearchMatchResponse::class.java)
                }
                view.showSearchMatch(data.await().event)
                view.hideLoading()
            } catch (e: RuntimeException){
                Log.e(SearchMatchPresenter::javaClass.toString(), e.localizedMessage)
            }
        }
    }
}