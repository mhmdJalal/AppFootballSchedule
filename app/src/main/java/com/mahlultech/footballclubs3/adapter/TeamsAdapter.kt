package com.mahlultech.footballclubs3.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.detailTeam.DetailTeamActivity
import com.mahlultech.footballclubs3.model.Teams
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class TeamsAdapter (private val context: Context, private val teams: List<Teams>): RecyclerView.Adapter<TeamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        return TeamViewHolder(TeamUI().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val team: Teams = teams[position]
        holder.bindItem(team)
        holder.itemView.setOnClickListener {
            context.startActivity<DetailTeamActivity>("idTeam" to team.idTeam)
        }
    }

}

class TeamUI : AnkoComponent<ViewGroup> {
    override fun createView(ui: AnkoContext<ViewGroup>): View  = with(ui){

        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            padding = dip(16)
            orientation = LinearLayout.HORIZONTAL

            imageView {
                id = R.id.team_badge
            }.lparams{
                height = dip(50)
                width = dip(50)
            }

            textView {
                id = R.id.team_name
                textSize = 16f
            }.lparams{
                margin = dip(15)
            }

        }
    }

}

class TeamViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val teamBadge: ImageView = view.findViewById(R.id.team_badge)
    private val teamName: TextView = view.findViewById(R.id.team_name)

    fun bindItem(teams: Teams){
        Picasso.get().load(teams.strTeamBadge).into(teamBadge)
        teamName.text = teams.strTeam
    }

}
