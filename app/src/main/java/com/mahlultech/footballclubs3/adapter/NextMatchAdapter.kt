package com.mahlultech.footballclubs3.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.model.Match
import com.mahlultech.footballclubs3.utils.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView

class NextMatchAdapter(private val matchs: List<Match>, private val listener: (Match) -> Unit): RecyclerView.Adapter<ViewHolderPast>(){
    private lateinit var reminderClickListener: (Match) -> Unit
    companion object {
        var enableReminder: Boolean = false
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPast {
        return ViewHolderPast(NextMatchItem().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int  = matchs.size

    override fun onBindViewHolder(holder: ViewHolderPast, position: Int) {
        holder.bindItem(matchs[position], listener)

        if (enableReminder) {
            holder.addReminder(matchs[position], reminderClickListener)
        }else {
            holder.removeReminder(matchs[position], reminderClickListener)
            disableReminder()
        }
    }

    fun enableReminder(listener: (Match) -> Unit){
        enableReminder = true
        reminderClickListener = listener
    }

    private fun disableReminder(){
        enableReminder = false
    }

}

@Suppress("DEPRECATION")
class NextMatchItem : AnkoComponent<ViewGroup> {

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

                    imageView {
                        id = R.id.add_to_calender
                    }.lparams(width = wrapContent, height = wrapContent){
                        alignParentRight()
                    }

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

class ViewHolderPast(view: View): RecyclerView.ViewHolder(view) {
    private val homeName: TextView = view.findViewById(R.id.home_name)
    private val awayName: TextView = view.findViewById(R.id.away_name)
    private val homeSkor: TextView = view.findViewById(R.id.home_skor)
    private val awaySkor: TextView = view.findViewById(R.id.away_skor)
    private val date: TextView = view.findViewById(R.id.tv_date)
    private val time: TextView = view.findViewById(R.id.tv_time)
    private val reminder: ImageView = view.findViewById(R.id.add_to_calender)

    @SuppressLint("SimpleDateFormat")
    fun bindItem(match: Match, listener: (Match) -> Unit) {
        homeName.text = match.strHomeTeam
        awayName.text = match.strAwayTeam
        homeSkor.text = match.intHomeScore
        awaySkor.text = match.intAwayScore
        time.text = match.strTime?.timeFormatter()
        date.text  = parseDate(match.dateEvent)
        itemView.setOnClickListener {
            listener(match)
        }
        if (NextMatchAdapter.enableReminder) reminder.setImageResource(R.drawable.ic_added_to_calender) else reminder.setImageResource(R.drawable.ic_add_to_calender)
    }

    fun addReminder(match: Match, reminderListener: (Match) -> Unit){
        reminder.setOnClickListener {
            reminderListener(match)
        }
    }

    fun removeReminder(match: Match, reminderListener: (Match) -> Unit){
        reminder.setOnClickListener {
            reminderListener(match)
        }
    }
}
