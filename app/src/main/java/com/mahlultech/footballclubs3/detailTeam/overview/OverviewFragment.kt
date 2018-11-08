package com.mahlultech.footballclubs3.detailTeam.overview

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.google.gson.Gson
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.api.ApiRepository
import com.mahlultech.footballclubs3.model.Teams
import com.mahlultech.footballclubs3.utils.invisible
import com.mahlultech.footballclubs3.utils.visible
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.ctx
import org.jetbrains.anko.support.v4.nestedScrollView

class OverviewFragment: Fragment(), AnkoComponent<Context>, Overview{
    private lateinit var team: Teams
    private lateinit var description: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var presenter: OverviewPresenter

    companion object {
        fun newInstance(idTeam: String): OverviewFragment{
            val fragment = OverviewFragment()
            val args = Bundle()
            args.putString("ID_TEAM", idTeam)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return createView(AnkoContext.create(ctx))
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val idTeam = arguments?.getString("ID_TEAM")

        val apiRepository = ApiRepository()
        val gson = Gson()
        presenter = OverviewPresenter(this, apiRepository, gson)
        presenter.getTeamDetail(idTeam)
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        nestedScrollView {
            lparams(width = matchParent, height = matchParent)

            relativeLayout {
                lparams(width = matchParent, height = matchParent)

                description = textView(){
                    id = R.id.descTeam
                    padding = dip(15)
                }.lparams(width = wrapContent, height = wrapContent)

                progressBar = progressBar {
                }.lparams {
                    centerHorizontally()
                }
            }

        }
    }

    override fun showLoading() {
        progressBar.visible()
    }

    override fun hideLoading() {
        progressBar.invisible()
    }

    override fun showOverview(data: List<Teams>) {
        team = data[0]
        description.text = team.strDescriptionEN
    }

}