package com.dicoding.footballapps.presenter

import android.content.Context
import com.dicoding.footballapps.api.ApiRepository
import com.dicoding.footballapps.api.TheSportDBApi
import com.dicoding.footballapps.model.MatchItemResponse
import com.dicoding.footballapps.utils.CoroutineContextProvider
import com.dicoding.footballapps.view.MatchEventView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.custom.async

class MatchPresenter(
    private val matchEventView: MatchEventView,
    private val apiRequest: ApiRepository,
    private val gson: Gson
) {

    fun getMatchPastData(idLeague: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            val dataMatch = gson.fromJson(
                apiRequest.doRequest(TheSportDBApi.getPastMatchEvent(idLeague)).await(),
                MatchItemResponse::class.java
            )

            matchEventView.showDataMatchList(dataMatch.events)
        }
    }

    fun getMatchNextData(idLeague: String?) {
        GlobalScope.launch(Dispatchers.Main) {
            val dataMatch = gson.fromJson(
                apiRequest
                    .doRequest(TheSportDBApi.getNextMatchEvent(idLeague)).await(),
                MatchItemResponse::class.java
            )

            matchEventView.showDataMatchList(dataMatch.events)
        }
    }
}
