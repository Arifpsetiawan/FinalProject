package com.dicoding.footballapps.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import com.dicoding.footballapps.R
import com.dicoding.footballapps.adapter.NextMatchAdapter
import com.dicoding.footballapps.api.ApiRepository
import com.dicoding.footballapps.model.MatchItem
import com.dicoding.footballapps.presenter.MatchPresenter
import com.dicoding.footballapps.view.MatchEventView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_nextmatch.view.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.onRefresh

class NextMatchFragment : Fragment(), MatchEventView {

    private var dataItems: MutableList<MatchItem> = mutableListOf()
    private var listener : OnFragmentInteractionListener? = null

    private lateinit var matchEventPresenter : MatchPresenter
    private lateinit var adapter             : NextMatchAdapter
    private lateinit var swipeRefreshLayout  : SwipeRefreshLayout
    private lateinit var progressBar         : ProgressBar
    private lateinit var spinner             : Spinner
    private lateinit var league              : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_nextmatch, container, false)
        spinner = view.spinner_nextMatch
        val spinnerItems = resources.getStringArray(R.array.league)
        val spinnerAdapter = ArrayAdapter(ctx, android.R.layout.simple_spinner_dropdown_item, spinnerItems)
        spinner.adapter = spinnerAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position) {
                    0 -> {
                        league = spinner.selectedItem.toString().replace("English Premier League", "4328")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    1 -> {
                        league = spinner.selectedItem.toString().replace("English League Championship", "4329")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    2 -> {
                        league = spinner.selectedItem.toString().replace("Spanish La Liga", "4335")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    3 -> {
                        league = spinner.selectedItem.toString().replace("Italian Serie A", "4332")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    4 -> {
                        league = spinner.selectedItem.toString().replace("French Ligue 1", "4334")
                        matchEventPresenter.getMatchNextData(league)
                    }
                    5 -> {
                        league = spinner.selectedItem.toString().replace("German Bundesliga", "4331")
                        matchEventPresenter.getMatchNextData(league)
                    }
                }
            }
        }

        val rv = view.findViewById<RecyclerView>(R.id.recyclerView_nextMatch)
        rv.layoutManager = LinearLayoutManager(context)
        adapter = NextMatchAdapter(dataItems, listener)
        rv.adapter = adapter

        swipeRefreshLayout  = view.swipeRefresh_match
        progressBar         = view.progressBar_match

        swipeRefreshLayout.onRefresh {
            matchEventPresenter.getMatchNextData(league)
        }
        showProgress()

        val api     = ApiRepository()
        val gson    = Gson()
        matchEventPresenter = MatchPresenter(this, api, gson )

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {

        fun onFragmentInteraction(item: MatchItem)
    }

    companion object {
        @JvmStatic
        fun newInstance() = NextMatchFragment()
    }

    override fun showProgress() {

        progressBar.visibility = View.VISIBLE

    }

    override fun hideProgress() {

        progressBar.visibility = View.GONE

    }

    override fun showDataMatchList(data: List<MatchItem>) {

        swipeRefreshLayout.isRefreshing = false
        dataItems.clear()
        dataItems.addAll(data)
        adapter.notifyDataSetChanged()
        hideProgress()
    }
}