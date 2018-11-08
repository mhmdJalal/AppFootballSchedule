package com.mahlultech.footballclubs3.detailPlayer

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.Player
import com.mahlultech.footballclubs3.model.PlayerDetailResponse
import com.mahlultech.footballclubs3.model.PlayerResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface DetailPlayerView{
    fun showLoading()
    fun hideLoading()
    fun showDetailPlayer(data: List<Player>)
}

class DetailPlayerPresenter(val view: DetailPlayerView,
                            val apiRepository: ApiRepository,
                            val gson: Gson,
                            val context: CoroutineContextProvider = CoroutineContextProvider()){

    fun getPlayerList(idPlayer: String?) {
        view.showLoading()

        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getPlayerDetail(idPlayer)),
                        PlayerDetailResponse::class.java)
            }

            view.showDetailPlayer(data.await().players)
            view.hideLoading()
        }
    }
}