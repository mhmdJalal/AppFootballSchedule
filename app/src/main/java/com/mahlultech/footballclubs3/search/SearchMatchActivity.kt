package com.mahlultech.footballclubs3.search

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.mahlultech.footballclubs3.MainActivity
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.adapter.MatchAdapter
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.visible
import kotlinx.android.synthetic.main.activity_search_match.*
import org.jetbrains.anko.startActivity

class SearchMatchActivity : AppCompatActivity(), SearchMatchView {
    private var matchs: MutableList<Match> = mutableListOf()
    private lateinit var adapter: MatchAdapter
    private lateinit var presenter: SearchMatchPresenter
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_match)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = MatchAdapter(this, matchs)
        recyclerView.adapter = adapter

        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = SearchMatchPresenter(this, apiRepository, gson)
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showSearchMatch(data: List<Match>) {
        matchs.clear()
        matchs.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity<MainActivity>()
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu_match, menu)

        val searchItem = menu.findItem(R.id.search_menu_match)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Telusuri"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                val match = Match()
                return when {
                    query.isEmpty() -> {
                        matchs.clear()
                        matchs.remove(match)
                        adapter.notifyDataSetChanged()
                        true
                    }
                    query.trim() == "" -> {
                        true
                    }
                    query.length > 3 -> {
                        presenter.searchMatch(query)
                        false
                    }
                    else -> {
                        matchs.clear()
                        matchs.remove(match)
                        adapter.notifyDataSetChanged()
                        true
                    }
                }
            }

            override fun onQueryTextChange(newText: String): Boolean {
                val match = Match()
                return when {
                    newText.isEmpty() -> {
                        matchs.clear()
                        matchs.remove(match)
                        adapter.notifyDataSetChanged()
                        true
                    }
                    newText.trim() == "" -> {
                        true
                    }
                    newText.length > 3 -> {
                        presenter.searchMatch(newText)
                        false
                    }
                    else -> {
                        matchs.clear()
                        matchs.remove(match)
                        adapter.notifyDataSetChanged()
                        true
                    }
                }
            }
        })

        searchView.setOnCloseListener {
            val match = Match()
            matchs.clear()
            matchs.remove(match)
            adapter.notifyDataSetChanged()
            false
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                startActivity<MainActivity>()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
