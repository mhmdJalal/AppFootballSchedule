package com.mahlultech.footballclubs3.detailTeam.players

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.model.Player
import com.mahlultech.footballclubs3.model.PlayerResponse
import com.mahlultech.footballclubs3.utils.CoroutineContextProvider
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg

interface PlayerView {
    fun showLoading()
    fun hideLoading()
    fun showPlayerList(data: List<Player>)
}

class PlayerPresenter(val view: PlayerView,
                      val apiRepository: ApiRepository,
                      val gson: Gson,
                      val context: CoroutineContextProvider = CoroutineContextProvider()) {

    fun getPlayerList(idTeam: String?) {
        view.showLoading()

        async(context.main){
            val data = bg {
                gson.fromJson(apiRepository
                        .doRequest(TheSportDBApi.getPlayerList(idTeam)),
                        PlayerResponse::class.java)
            }

            view.showPlayerList(data.await().players)
            view.hideLoading()
        }
    }

}