package com.mahlultech.footballclubs3.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.dbHelper.FavoriteTeam
import com.mahlultech.footballclubs3.detailTeam.DetailTeamActivity
import com.squareup.picasso.Picasso
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.startActivity

class FavoriteTeamAdapter(private val context: Context, private val favoriteTeams: List<FavoriteTeam>): RecyclerView.Adapter<FavTeamHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavTeamHolder {
        return FavTeamHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = favoriteTeams.size

    override fun onBindViewHolder(holder: FavTeamHolder, position: Int) {
        val team: FavoriteTeam = favoriteTeams[position]
        holder.bindItem(team)
        holder.itemView.setOnClickListener {
            context.startActivity<DetailTeamActivity>("idTeam" to team.teamId)
        }
    }

}

class FavTeamHolder(view: View): RecyclerView.ViewHolder(view) {
    private val teamBadge: ImageView = view.findViewById(R.id.team_badge)
    private val teamName: TextView = view.findViewById(R.id.team_name)

    fun bindItem(teams: FavoriteTeam){
        Picasso.get().load(teams.badge).into(teamBadge)
        teamName.text = teams.name
    }
}
