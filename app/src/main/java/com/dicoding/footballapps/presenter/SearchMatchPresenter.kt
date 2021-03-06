package com.dicoding.footballapps.presenter

import android.content.Context
import com.dicoding.footballapps.api.ApiRepository
import com.dicoding.footballapps.api.TheSportDBApi
import com.dicoding.footballapps.model.EventResponse
import com.dicoding.footballapps.utils.CoroutineContextProvider
import com.dicoding.footballapps.view.SearchMatchView
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.coroutines.experimental.bg

class SearchMatchPresenter(
    private val view: SearchMatchView,
    private val apiRequest: ApiRepository,
    private val gson: Gson
) {

    fun getSearchEvent(idSearch: String?) {
        view.showProgress()
        GlobalScope.launch(Dispatchers.Main) {
            val data = gson.fromJson(
                apiRequest.doRequest(TheSportDBApi.getSearchMatch(idSearch)).await(),
                EventResponse::class.java
            )

            view.showListEvent(data.event)
            view.hideProgress()
        }
    }
}
