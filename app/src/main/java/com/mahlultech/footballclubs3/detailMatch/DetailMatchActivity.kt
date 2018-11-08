package com.mahlultech.footballclubs3.detailMatch

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.dbHelper.FavoriteEvent
import com.mahlultech.footballclubs3.dbHelper.database
import com.mahlultech.footballclubs3.model.AwayTeam
import com.mahlultech.footballclubs3.model.HomeTeam
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.utils.*
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

@Suppress("DEPRECATION")
class DetailMatchActivity: AppCompatActivity(), MatchDetailView {

    private var match: Match = Match()
    private lateinit var homeBadge: ImageView
    private lateinit var awayBadge: ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var homeDefense: TextView
    private lateinit var awayDefense: TextView
    private lateinit var homeGoal: TextView
    private lateinit var awayGoal: TextView
    private lateinit var homeMidfield: TextView
    private lateinit var awayMidfield: TextView
    private lateinit var homeForward: TextView
    private lateinit var awayForward: TextView
    private lateinit var homeSubstitues: TextView
    private lateinit var awaySubstitues: TextView
    private lateinit var date: TextView
    private lateinit var homeName: TextView
    private lateinit var homeSkor: TextView
    private lateinit var homeFormation: TextView
    private lateinit var awayName: TextView
    private lateinit var awaySkor: TextView
    private lateinit var awayFormation: TextView
    private lateinit var homeShots: TextView
    private lateinit var homeKeeper: TextView
    private lateinit var awayShots: TextView
    private lateinit var awayKeeper: TextView
    private lateinit var presenter: MatchDetailPresenter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var homeTeam: HomeTeam
    private lateinit var awayTeam: AwayTeam
    private lateinit var idEvent: String
    private lateinit var idHome: String
    private lateinit var idAway: String
    private lateinit var time: TextView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idEvent = intent.getStringExtra("idEvent")
        idHome = intent.getStringExtra("idHome")
        idAway = intent.getStringExtra("idAway")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Match Detail"

        /* Layout UI */
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            orientation = LinearLayout.VERTICAL

            swipeRefreshLayout = swipeRefreshLayout {

                scrollView {
                    lparams(width = matchParent, height = matchParent)

                        linearLayout {
                            lparams(width = matchParent, height = matchParent)
                            orientation = LinearLayout.VERTICAL

                            date = textView{
                                id = R.id.tv_date
                                textSize = 15f
                                typeface = regular
                                textColor = resources.getColor(R.color.colorPrimary)
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(20)
                                gravity = Gravity.CENTER_HORIZONTAL
                            }

                            time = textView{
                                id = R.id.tv_time
                                textSize = 15f
                                typeface = regular
                                textColor = resources.getColor(R.color.colorPrimary)
                            }.lparams {
                                width = wrapContent
                                height = wrapContent
                                topMargin = dip(10)
                                gravity = Gravity.CENTER_HORIZONTAL
                            }

                            relativeLayout {R.color.colorPrimary
                                id = R.id.relative1
                                lparams {
                                    width = matchParent
                                    height = wrapContent
                                    topMargin = dip(20)
                                    leftMargin = dip(20)
                                    rightMargin = dip(20)
                                }

                                progressBar = progressBar {
                                }.lparams {
                                    centerHorizontally()
                                }

                                linearLayout {
                                    orientation = LinearLayout.VERTICAL
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    /* Home logo */
                                    homeBadge = imageView{
                                        id = R.id.home_logo
                                    }.lparams {
                                        width = dip(80)
                                        height = dip(80)
                                    }

                                    /* Home Club Name */
                                    homeName = textView {
                                        textSize = 18f
                                        typeface = regular
                                        ellipsize = TextUtils.TruncateAt.END
                                        maxLines = 1
                                        textColor = resources.getColor(R.color.colorPrimary)
                                    }.lparams {
                                        width = wrapContent
                                        height = wrapContent
                                    }

                                    /* Home Formation */
                                    homeFormation = textView {
                                        textSize = 18f
                                        typeface = regular
                                    }.lparams {
                                        width = wrapContent
                                        height = wrapContent
                                    }
                                }.lparams(width = wrapContent, height = wrapContent){
                                    alignParentLeft()
                                    leftMargin = dip(15)
                                }

                                homeSkor = textView{
                                    id = R.id.home_skor
                                    textSize = 30f
                                    typeface = bold
                                }.lparams {
                                    width = wrapContent
                                    height = wrapContent
                                    leftOf(R.id.tv_match)
                                    rightMargin = dip(20)
                                }

                                textView("VS"){
                                    id = R.id.tv_match
                                    textSize = 20f
                                    typeface = regular
                                }.lparams {
                                    width = wrapContent
                                    height = wrapContent
                                    centerHorizontally()
                                    topMargin = dip(5)
                                }

                                awaySkor = textView {
                                    id = R.id.away_skor
                                    textSize = 30f
                                    typeface = bold
                                }.lparams {
                                    width = wrapContent
                                    height = wrapContent
                                    rightOf(R.id.tv_match)
                                    leftMargin = dip(20)
                                }

                                linearLayout {
                                    orientation = LinearLayout.VERTICAL
                                    gravity = Gravity.CENTER_HORIZONTAL

                                    /* Away logo */
                                    awayBadge = imageView{
                                        id = R.id.away_logo
                                    }.lparams {
                                        width = dip(80)
                                        height = dip(80)
                                    }

                                    /* Away Club name */
                                    awayName = textView {
                                        textSize = 18f
                                        typeface = regular
                                        ellipsize = TextUtils.TruncateAt.END
                                        maxLines = 1
                                        textColor = resources.getColor(R.color.colorPrimary)
                                    }.lparams {
                                        width = wrapContent
                                        height = wrapContent
                                    }

                                    /* Away Formation */
                                    awayFormation = textView {
                                        textSize = 18f
                                        typeface = regular

                                    }.lparams {
                                        width = wrapContent
                                        height = wrapContent
                                    }
                                }.lparams(width = wrapContent, height = wrapContent){
                                    alignParentRight()
                                    rightMargin = dip(15)
                                }
                            }

                            view {
                                id = R.id.line1
                                backgroundColor = Color.DKGRAY
                            }.lparams {
                                width = matchParent
                                height = dip(1)
                                topMargin = dip(8)
                                leftMargin = dip(20)
                                rightMargin = dip(20)
                            }

                            /* Goals */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL

                                homeGoal = textView {
                                    id = R.id.home_goals
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0),height = wrapContent){
                                    weight = 2f
                                    bottomMargin = dip(10)
                                }

                                textView("Goals") {
                                    id = R.id.goals
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                awayGoal = textView {
                                    id = R.id.away_goals
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    weight = 2f
                                    bottomMargin = dip(10)
                                }
                            }
                            /* ================================= End of Goals ================================== */

                            /* Shots */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL

                                homeShots = textView {
                                    id = R.id.home_shots
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                                textView("Shots") {
                                    id = R.id.shots
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                awayShots = textView {
                                    id = R.id.away_shots
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }
                            }
                            /* ================================= End of Shots ================================== */

                            view {
                                id = R.id.line2
                                backgroundColor = Color.DKGRAY
                            }.lparams {
                                width = matchParent
                                height = dip(1)
                                topMargin = dip(8)
                                leftMargin = dip(20)
                                rightMargin = dip(20)
                            }

                            textView("Lineups") {
                                id = R.id.lineups
                                textSize = 18f
                                typeface = regular
                            }.lparams(width = wrapContent, height = wrapContent){
                                gravity = Gravity.CENTER_HORIZONTAL
                                bottomMargin = dip(10)
                            }

                            /* Goal Keepers */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL

                                homeKeeper = textView {
                                    id = R.id.home_goalkeeper
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                textView("Goal Keepers") {
                                    id = R.id.goalkeeper
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = wrapContent, height = wrapContent){
                                    bottomMargin = dip(10)
                                }

                                awayKeeper = textView {
                                    id = R.id.away_goalkeeper
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                            }
                            /* ================================= End of Goalkeepers ================================== */

                            /* Defense */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL


                                homeDefense = textView {
                                    id = R.id.home_defense
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                                textView("Defense") {
                                    id = R.id.defense
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                awayDefense = textView {
                                    id = R.id.away_defense
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                            }
                            /* ================================= End of Defense ================================== */

                            /* Midfield */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL

                                homeMidfield = textView {
                                    id = R.id.home_midfield
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                                textView("Midfield") {
                                    id = R.id.midfield
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                awayMidfield = textView {
                                    id = R.id.away_midfield
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                            }
                            /* ================================= End of Midfield ================================== */

                            /* Forward */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL

                                homeForward = textView {
                                    id = R.id.home_forward
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                                textView("Forward") {
                                    id = R.id.forward
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                awayForward = textView {
                                    id = R.id.away_forward
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                            }
                            /* ================================= End of Forward ================================== */

                            /* Substitues */
                            linearLayout {
                                lparams{
                                    width = matchParent
                                    height = wrapContent
                                    leftPadding = dip(20)
                                    rightPadding = dip(20)
                                }
                                orientation = LinearLayout.HORIZONTAL

                                homeSubstitues = textView {
                                    id = R.id.home_substitues
                                    gravity = Gravity.START
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                                textView("Substitues") {
                                    textColor = resources.getColor(R.color.colorPrimary)
                                    gravity = Gravity.CENTER_HORIZONTAL
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 1f
                                }

                                awaySubstitues = textView {
                                    id = R.id.away_substitues
                                    gravity = Gravity.END
                                    typeface = regular
                                }.lparams(width = dip(0), height = wrapContent){
                                    bottomMargin = dip(10)
                                    weight = 2f
                                }

                            }
                            /* ================================= End of Substitues ================================== */

                        }

                    }
            }.lparams(width = matchParent, height = matchParent)

        }

        favoriteState()
        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = MatchDetailPresenter(this, apiRepository, gson)
        presenter.getTeamAway(idAway)
        presenter.getTeamHome(idHome)
        presenter.getMatchDetail(idEvent)
        swipeRefreshLayout.onRefresh {
            presenter.getTeamAway(idAway)
            presenter.getTeamHome(idHome)
            presenter.getMatchDetail(idEvent)
        }
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun showTeamHome(data: List<HomeTeam>) {
        homeTeam = HomeTeam(data[0].teamId, data[0].teamName, data[0].teamBadge)
        swipeRefreshLayout.isRefreshing = false
        Picasso.get().load(homeTeam.teamBadge).into(homeBadge)
    }

    override fun showTeamAway(data: List<AwayTeam>) {
        awayTeam = AwayTeam(data[0].teamId, data[0].teamName, data[0].teamBadge)
        swipeRefreshLayout.isRefreshing = false
        Picasso.get().load(awayTeam.teamBadge).into(awayBadge)
    }

    override fun showDetailMatch(data: List<Match>) {
        match = Match(data[0].idEvent, data[0].idHomeTeam, data[0].idAwayTeam, data[0].strHomeTeam, data[0].strAwayTeam, data[0].intHomeScore, data[0].intAwayScore, data[0].dateEvent, data[0].strTime)
        swipeRefreshLayout.isRefreshing = false
        homeDefense.text = data[0].strHomeLineupDefense
        homeGoal.text = data[0].strHomeGoalDetails
        homeMidfield.text = data[0].strHomeLineupMidfield
        homeForward.text = data[0].strHomeLineupForward
        homeSubstitues.text = data[0].strHomeLineupSubstitutes
        homeSkor.text = data[0].intHomeScore
        homeFormation.text = data[0].strHomeFormation
        homeName.text = data[0].strHomeTeam
        homeKeeper.text = data[0].strHomeLineupGoalkeeper
        homeShots.text = data[0].intHomeShots
        awayShots.text = data[0].intAwayShots
        awaySkor.text = data[0].intAwayScore
        awayFormation.text = data[0].strAwayFormation
        awayName.text = data[0].strAwayTeam
        awayKeeper.text = data[0].strAwayLineupGoalkeeper
        awayDefense.text = data[0].strAwayLineupDefense
        awayGoal.text = data[0].strAwayGoalDetails
        awayMidfield.text = data[0].strAwayLineupMidfield
        awayForward.text = data[0].strAwayLineupForward
        awaySubstitues.text = data[0].strAwayLineupSubstitutes
        date.text = parseDate(data[0].dateEvent)
        time.text = data[0].strTime?.timeFormatter()
    }

    private fun favoriteState(){
        database.use {
            val result = select(FavoriteEvent.TABLE_FAVORITE)
                    .whereArgs("(EVENT_ID = {id})",
                            "id" to idEvent)
            val favorite = result.parseList(classParser<FavoriteEvent>())
            if (!favorite.isEmpty()) isFavorite = true
        }
    }

    private fun addToFavorite(){
        try {
            database.use {
                insert(FavoriteEvent.TABLE_FAVORITE,
                        FavoriteEvent.EVENT_ID to match.idEvent,
                        FavoriteEvent.ID_HOME to match.idHomeTeam,
                        FavoriteEvent.ID_AWAY to match.idAwayTeam,
                        FavoriteEvent.HOME_NAME to match.strHomeTeam,
                        FavoriteEvent.HOME_SKOR to match.intHomeScore,
                        FavoriteEvent.AWAY_NAME to match.strAwayTeam,
                        FavoriteEvent.AWAY_SKOR to match.intAwayScore,
                        FavoriteEvent.DATE to match.dateEvent,
                        FavoriteEvent.TIME to time.text)
            }
            snackbar(swipeRefreshLayout, "Added to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefreshLayout, e.localizedMessage).show()
        }
    }

    private fun removeFromFavorite(){
        try {
            database.use {
                delete(FavoriteEvent.TABLE_FAVORITE, "(EVENT_ID = {id})",
                        "id" to  idEvent)
            }
            snackbar(swipeRefreshLayout, "Removed to favorite").show()
        } catch (e: SQLiteConstraintException){
            snackbar(swipeRefreshLayout, e.localizedMessage).show()
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