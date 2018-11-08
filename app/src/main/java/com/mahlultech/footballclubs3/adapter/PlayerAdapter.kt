package com.mahlultech.footballclubs3.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.mahlultech.footballclubs3.R
import com.mahlultech.footballclubs3.detailPlayer.DetailPlayerActivity
import com.mahlultech.footballclubs3.model.Player
import com.mahlultech.footballclubs3.utils.regular
import com.squareup.picasso.Picasso
import org.jetbrains.anko.*

class PlayerAdapter(private val context: Context, private val players: List<Player>): RecyclerView.Adapter<ViewHolderPlayer>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlayer {
        return ViewHolderPlayer(PlayerList().createView(AnkoContext.create(parent.context, parent)))
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: ViewHolderPlayer, position: Int) {
        val player = players[position]
        holder.bindItem(players[position])
        holder.itemView.setOnClickListener {
            context.startActivity<DetailPlayerActivity>("idPlayer" to player.idPlayer, "playerName" to player.strPlayer)
        }
    }
}

class PlayerList: AnkoComponent<ViewGroup> {

    override fun createView(ui: AnkoContext<ViewGroup>): View  = with(ui){

        linearLayout {
            lparams(width = matchParent, height = wrapContent)
            orientation = LinearLayout.HORIZONTAL
            padding = dip(10)

            imageView {
                id = R.id.player_img
            }.lparams(width = dip(50), height = dip(50))

            textView {
                id = R.id.player_name
                typeface = regular
            }.lparams(width = dip(0), height = wrapContent){
                weight = 2f
                gravity = Gravity.CENTER_VERTICAL
                leftMargin = dip(10)
            }

            textView {
                id = R.id.player_position
                typeface = regular
            }.lparams(width = dip(0), height = wrapContent){
                weight = 1f
                gravity = Gravity.CENTER_VERTICAL
            }
        }

    }
}

class ViewHolderPlayer(view: View): RecyclerView.ViewHolder(view) {
    private val player_img: ImageView = view.findViewById(R.id.player_img)
    private val player_name: TextView = view.findViewById(R.id.player_name)
    private val player_position: TextView = view.findViewById(R.id.player_position)

    fun bindItem(player: Player) {
        Picasso.get().load(player.strCutout).into(player_img)
        player_name.text = player.strPlayer
        player_position.text = player.strPosition
    }
}
