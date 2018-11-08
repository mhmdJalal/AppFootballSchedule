package com.mahlultech.footballclubs3.detailTeam

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.api.TheSportDBApi
import com.mahlultech.footballclubs3.dbHelper.FavoriteTeam
import com.mahlultech.footballclubs3.dbHelper.database
import com.mahlultech.footballclubs3.detailTeam.overview.OverviewFragment
import com.mahlultech.footballclubs3.detailTeam.players.PlayersFragment
import com.mahlultech.footballclubs3.model.Teams
import com.mahlultech.footballclubs3.model.TeamsResponse
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail_team.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class DetailTeamActivity : AppCompatActivity(){
    private lateinit var teams: List<Teams>
    private lateinit var team: Teams
    private lateinit var teamsResponse: TeamsResponse
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var idTeam: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_team)
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        idTeam = intent.getStringExtra("idTeam")

        val adapter = TeamsPagerAdapter(supportFragmentManager)

        adapter.addFragment(OverviewFragment.newInstance(idTeam), "Overview")
        adapter.addFragment(PlayersFragment.newInstance(idTeam), "Players")
        viewPager.adapter = adapter

        tablayout.setupWithViewPager(viewPager)

        favoriteState()
        val apiRepository = ApiRepository()
        val gson = Gson()

        doAsync {
            teamsResponse = gson.fromJson(apiRepository
                    .doRequest(TheSportDBApi.getDetailTeam(idTeam)),
                    TeamsResponse::class.java)

            uiThread {
                teams = teamsResponse.teams
                team = teams[0]

                Picasso.get().load(team.strTeamBadge).into(iv_teamBadge)
                tv_teamName.text = team.strTeam
                tv_teamYear.text = team.intFormedYear
                tv_teamStadium.text = team.strStadium
            }
        }
    }

    internal inner class TeamsPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment  = mFragmentList[position]

        override fun getCount(): Int  = mFragmentList.size

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList[position]
        }
    }

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteTeam.TABLE_TEAM)
                    .whereArgs("(TEAM_ID = {id})",
                            "id" to idTeam)
            val favorite = result.parseList(classParser<FavoriteTeam>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite() = try {
        database.use {
            insert(FavoriteTeam.TABLE_TEAM,
                    FavoriteTeam.TEAM_ID to team.idTeam,
                    FavoriteTeam.TEAM_NAME to team.strTeam,
                    FavoriteTeam.TEAM_BADGE to team.strTeamBadge)
        }
        snackbar(viewPager, "Added to favorite").show()
    } catch (e: SQLiteConstraintException){
        snackbar(viewPager, e.localizedMessage).show()
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(FavoriteTeam.TABLE_TEAM, "(TEAM_ID = {id})",
                        "id" to  idTeam)
            }
            snackbar(viewPager, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(viewPager, e.localizedMessage).show()
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_filled)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        menuItem = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            R.id.add_to_favorite -> {
                if (isFavorite) removeFromFavorite() else addToFavorite()
                isFavorite = !isFavorite
                setFavorite()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
