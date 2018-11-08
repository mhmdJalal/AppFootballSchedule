package com.mahlultech.footballclubs3.dbHelper

data class FavoriteTeam(val id: Long?,
                        val teamId: String?,
                        val name: String?,
                        val badge: String?){
    companion object {
        const val TABLE_TEAM: String = "TABLE_TEAM"
        const val ID: String = "ID"
        const val TEAM_ID: String = "TEAM_ID"
        const val TEAM_NAME: String = "TEAM_NAME"
        const val TEAM_BADGE: String = "TEAM_BADGE"
    }
}