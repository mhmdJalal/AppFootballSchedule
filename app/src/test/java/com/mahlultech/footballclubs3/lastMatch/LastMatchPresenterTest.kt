package com.mahlultech.footballclubs3.lastMatch

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.matchs.lastMatch.LastMatchPresenter
import com.mahlultech.footballclubs3.matchs.lastMatch.LastMatchView
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.model.MatchResponse
import com.mahlultech.footballclubs3.utils.TestContextProvider
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class LastMatchPresenterTest {

    @Mock
    private lateinit var view: LastMatchView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    private lateinit var presenter: LastMatchPresenter

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        presenter = LastMatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetLastMatch() {
        val matchs: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matchs)
        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getLastMatch("")),
                MatchResponse::class.java
        )).thenReturn(response)

        presenter.getLastMatch("")

        view.showLoading()
        view.showLastMatch(matchs)
        view.hideLoading()
    }
}