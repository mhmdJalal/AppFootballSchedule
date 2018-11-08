package com.mahlultech.footballclubs3.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.dbHelper.FavoriteEvent
import com.mahlultech.footballclubs3.detailMatch.DetailMatchActivity
import com.mahlultech.footballclubs3.utils.parseDate
import com.mahlultech.footballclubs3.utils.timeFormatter
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.startActivity

class FavoriteMatchAdapter(private val context: Context, private val favoriteEvents: List<FavoriteEvent>): RecyclerView.Adapter<FavoriteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder(EventList().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int  = favoriteEvents.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favorite = favoriteEvents[position]
        holder.bindItem(favoriteEvents[position])
        holder.itemView.setOnClickListener {
            context.startActivity<DetailMatchActivity>("idEvent" to favorite.eventId, "idAway" to favorite.idAway, "idHome" to favorite.idHome)
        }
    }

}

class FavoriteViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val homeName: TextView = view.findViewById(R.id.home_name)
    private val awayName: TextView = view.findViewById(R.id.away_name)
    private val homeSkor: TextView = view.findViewById(R.id.home_skor)
    private val awaySkor: TextView = view.findViewById(R.id.away_skor)
    private val date: TextView = view.findViewById(R.id.tv_date)
    private val time: TextView = view.findViewById(R.id.tv_time)

    fun bindItem(favoriteEvent: FavoriteEvent) {
        homeName.text = favoriteEvent.homeName
        awayName.text = favoriteEvent.awayName
        homeSkor.text = favoriteEvent.homeSkor
        awaySkor.text = favoriteEvent.awaySkor
        time.text = favoriteEvent.time
        date.text  = parseDate(favoriteEvent.date)
    }
}
