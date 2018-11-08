package com.mahlultech.footballclubs3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.detailMatch.DetailMatchActivity
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.utils.bold
import com.mahlultech.footballclubs3.utils.parseDate
import com.mahlultech.footballclubs3.utils.regular
import com.mahlultech.footballclubs3.utils.timeFormatter
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import java.util.*

class MatchAdapter(private val context: Context, private val matchs: List<Match>): RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EventList().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = matchs.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matchs[position]
        holder.bindItem(matchs[position])
        holder.itemView.setOnClickListener {
            context.startActivity<DetailMatchActivity>("idEvent" to match.idEvent, "idAway" to match.idAwayTeam, "idHome" to match.idHomeTeam)
        }
    }

}

@Suppress("DEPRECATION")
class EventList : AnkoComponent<ViewGroup> {

    @SuppressLint("RtlHardcoded", "ResourceType")
    override fun createView(ui: AnkoContext<ViewGroup>): View {
        return with(ui) {

            cardView {
                lparams {
                    width = matchParent
                    height = wrapContent
                    topMargin = dip(5)
                    leftMargin = dip(15)
                    rightMargin = dip(15)
                    bottomMargin = dip(5)
                }
                backgroundColor = Color.WHITE
                radius = 10f

                relativeLayout {
                    lparams(width = matchParent, height = wrapContent)
                    padding = dip(20)

                    textView {
                        id = R.id.tv_date
                        textColor = resources.getColor(R.color.colorPrimary)
                        typeface = regular
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        centerHorizontally()
                    }

                    textView {
                        id = R.id.tv_time
                        textColor = resources.getColor(R.color.colorPrimary)
                        typeface = regular
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(7)
                        below(R.id.tv_date)
                        centerHorizontally()
                    }

                    textView {
                        id = R.id.home_name
                        textSize = 17f
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 1
                        typeface = regular
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(7)
                        leftOf(R.id.home_skor)
                        below(R.id.tv_time)
                    }

                    textView {
                        id = R.id.home_skor
                        textSize = 17f
                        typeface = bold
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(7)
                        leftMargin = dip(20)
                        leftOf(R.id.tv_match)
                        below(R.id.tv_time)
                    }

                    textView("VS") {
                        id = R.id.tv_match
                        textSize = 17f
                        typeface = regular
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(7)
                        leftMargin = dip(10)
                        rightMargin = dip(10)
                        below(R.id.tv_time)
                        centerHorizontally()
                    }

                    textView {
                        id = R.id.away_skor
                        textSize = 17f
                        typeface = bold
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(7)
                        rightMargin = dip(20)
                        rightOf(R.id.tv_match)
                        below(R.id.tv_time)
                    }

                    textView {
                        id = R.id.away_name
                        textSize = 17f
                        ellipsize = TextUtils.TruncateAt.END
                        maxLines = 1
                        typeface = regular
                    }.lparams {
                        width = wrapContent
                        height = wrapContent
                        topMargin = dip(7)
                        rightOf(R.id.away_skor)
                        below(R.id.tv_time)
                    }
                }
            }
        }
    }
}

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val homeName: TextView = view.findViewById(R.id.home_name)
    private val awayName: TextView = view.findViewById(R.id.away_name)
    private val homeSkor: TextView = view.findViewById(R.id.home_skor)
    private val awaySkor: TextView = view.findViewById(R.id.away_skor)
    private val date: TextView = view.findViewById(R.id.tv_date)
    private val time: TextView = view.findViewById(R.id.tv_time)

    @SuppressLint("SimpleDateFormat")
    fun bindItem(match: Match) {
        homeName.text = match.strHomeTeam
        awayName.text = match.strAwayTeam
        homeSkor.text = match.intHomeScore
        awaySkor.text = match.intAwayScore
        time.text = match.strTime?.timeFormatter()
        date.text  = parseDate(match.dateEvent)
    }
}