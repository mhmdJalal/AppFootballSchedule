package com.mahlultech.footballclubs3.detailPlayer

import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.model.Player
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.regular
import com.mahlultech.footballclubs3.utils.visible
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

class DetailPlayerActivity: AppCompatActivity(), DetailPlayerView{
    private lateinit var player: Player
    private lateinit var presenter: DetailPlayerPresenter

    private lateinit var ivPlayer: ImageView
    private lateinit var tvWeight: TextView
    private lateinit var tvHeight: TextView
    private lateinit var tvPosition: TextView
    private lateinit var tvDescription: TextView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private lateinit var idPlayer: String
    private lateinit var playerName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        idPlayer = intent.getStringExtra("idPlayer")
        playerName = intent.getStringExtra("playerName")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = playerName

        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = DetailPlayerPresenter(this, apiRepository, gson)

        /* Layout UI */
        linearLayout {
            lparams(width = matchParent, height = matchParent)
            orientation = LinearLayout.VERTICAL

            swipeRefreshLayout = swipeRefreshLayout {

                scrollView {
                    lparams(width = matchParent, height = matchParent)

                    linearLayout{
                        lparams(width = matchParent, height = matchParent)
                        orientation = LinearLayout.VERTICAL

                        ivPlayer = imageView(){
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(width = matchParent, height = dip(200))

                        linearLayout {
                            lparams(width = matchParent, height = wrapContent){
                                topMargin = dip(10)
                            }
                            orientation = LinearLayout.HORIZONTAL

                            linearLayout {
                                lparams(width = dip(0), height = wrapContent){
                                    weight = 1f
                                }
                                orientation = LinearLayout.VERTICAL

                                textView {
                                    text = context.getString(R.string.weight)
                                    typeface = regular
                                }.lparams(width = wrapContent, height = wrapContent){
                                    gravity = Gravity.CENTER_HORIZONTAL
                                }

                                tvWeight = textView("0.0") {
                                    textSize = 30f
                                    typeface = regular
                                }.lparams(width = wrapContent, height = wrapContent){
                                    gravity = Gravity.CENTER_HORIZONTAL
                                }
                            }

                            linearLayout {
                                lparams(width = dip(0), height = wrapContent){
                                    weight = 1f
                                }
                                orientation = LinearLayout.VERTICAL

                                textView {
                                    text = context.getString(R.string.height)
                                    typeface = regular
                                }.lparams(width = wrapContent, height = wrapContent){
                                    gravity = Gravity.CENTER_HORIZONTAL
                                }

                                tvHeight = textView("0.0") {
                                    textSize = 30f
                                    typeface = regular
                                }.lparams(width = wrapContent, height = wrapContent){
                                    gravity = Gravity.CENTER_HORIZONTAL
                                }
                            }
                        }

                        tvPosition = textView("-") {
                            textSize = 15f
                            typeface = regular
                        }.lparams(width = wrapContent, height = wrapContent){
                            gravity = Gravity.CENTER_HORIZONTAL
                        }

                        view {
                            backgroundColor = Color.DKGRAY
                        }.lparams {
                            width = matchParent
                            height = dip(1)
                            topMargin = dip(5)
                            leftMargin = dip(20)
                            rightMargin = dip(20)
                            bottomMargin = dip(3)
                        }

                        tvDescription = textView("no description") {
                            typeface = regular
                            padding = dip(10)
                        }.lparams(width = matchParent, height = wrapContent){
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                }

            }.lparams(width = matchParent, height = matchParent)
        }

        presenter.getPlayerList(idPlayer)
        swipeRefreshLayout.onRefresh {
            presenter.getPlayerList(idPlayer)
        }
    }

    override fun showLoading() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideLoading() {
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showDetailPlayer(data: List<Player>) {
        player = data[0]
        when {
            player.strFanart1 != null -> Picasso.get().load(player.strFanart1).fit().into(ivPlayer)
            player.strFanart2 != null -> Picasso.get().load(player.strFanart2).fit().into(ivPlayer)
            player.strFanart3 != null -> Picasso.get().load(player.strFanart3).fit().into(ivPlayer)
            player.strFanart4 != null -> Picasso.get().load(player.strFanart4).fit().into(ivPlayer)
            else -> Picasso.get().load("https://www.mahindralifespaces.com/media/2549/no-img-02.jpg").fit().into(ivPlayer)
        }

        if (player.strWeight != null || player.strWeight !="") tvWeight.text = player.strWeight else tvWeight.text = "0.0"
        if (player.strHeight != null || player.strHeight != "") tvHeight.text = player.strHeight else tvHeight.text = "0.0"
        if (player.strPosition != null || player.strPosition != "") tvPosition.text = player.strPosition else tvPosition.text = "-"
        if (player.strDescriptionEN != null || player.strDescriptionEN != "") tvDescription.text = player.strDescriptionEN else tvDescription.text = "-"
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}