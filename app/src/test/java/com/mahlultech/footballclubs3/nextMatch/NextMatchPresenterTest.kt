package com.mahlultech.footballclubs3.nextMatch

import com.google.gson.Gson
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.matchs.nextMatch.NextMatchPresenter
import com.mahlultech.footballclubs3.matchs.nextMatch.NextMatchView
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.model.MatchResponse
import com.mahlultech.footballclubs3.utils.TestContextProvider
import org.junit.Test

import org.junit.Before
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class NextMatchPresenterTest {

    @Mock
    private lateinit var view: NextMatchView

    @Mock
    private lateinit var gson: Gson

    @Mock
    private lateinit var apiRepository: ApiRepository

    private lateinit var presenter: NextMatchPresenter

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        presenter = NextMatchPresenter(view, apiRepository, gson, TestContextProvider())
    }

    @Test
    fun testGetNextMatch() {
        val matchs: MutableList<Match> = mutableListOf()
        val response = MatchResponse(matchs)
        `when`(gson.fromJson(apiRepository
                .doRequest(TheSportDBApi.getNextMatch("")),
                MatchResponse::class.java
        )).thenReturn(response)

        presenter.getNextMatch("")

        verify(view).showLoading()
        verify(view).showNextMatch(matchs)
        verify(view).hideLoading()
    }
}