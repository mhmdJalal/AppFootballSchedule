package com.mahlultech.footballclubs3.dbHelper

data class FavoriteEvent(val id: Long?,
                         val eventId: String?,
                         val idHome: String?,
                         val idAway:String?,
                         val date: String?,
                         val homeName: String?,
                         val homeSkor: String?,
                         val awayName: String?,
                         val awaySkor: String?,
                         val time: String?) {
    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID"
        const val EVENT_ID: String = "EVENT_ID"
        const val DATE: String = "DATE"
        const val HOME_NAME: String = "HOME_NAME"
        const val HOME_SKOR: String = "HOME_SKOR"
        const val AWAY_NAME: String = "AWAY_NAME"
        const val AWAY_SKOR: String = "AWAY_SKOR"
        const val ID_HOME: String = "ID_HOME"
        const val ID_AWAY: String = "ID_AWAY"
        const val TIME: String = "TIME"
    }
}