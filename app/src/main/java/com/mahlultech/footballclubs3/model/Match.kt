package com.mahlultech.footballclubs3.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class MatchResponse(
        @SerializedName("events")
        val events: List<Match>
)

data class SearchMatchResponse(
        @SerializedName("event")
        val event: List<Match>
)

data class Match(
        @SerializedName("idEvent")
        val idEvent: String? = null,

        @SerializedName("idHomeTeam")
        val idHomeTeam: String? = null,

        @SerializedName("idAwayTeam")
        val idAwayTeam: String? = null,

        @SerializedName("strHomeTeam")
        val strHomeTeam: String? = null,

        @SerializedName("strAwayTeam")
        val strAwayTeam: String? = null,

        @SerializedName("intHomeScore")
        val intHomeScore: String? = null,

        @SerializedName("intAwayScore")
        val intAwayScore: String? = null,

        @SerializedName("dateEvent")
        val dateEvent: String? = null,

        @SerializedName("strHomeGoalDetails")
        val strHomeGoalDetails: String? = null,

        @SerializedName("strAwayGoalDetails")
        val strAwayGoalDetails: String? = null,

        @SerializedName("strHomeRedCards")
        val strHomeRedCards: String? = null,

        @SerializedName("strAwayRedCards")
        val strAwayRedCards: String? = null,

        @SerializedName("strHomeYellowCards")
        val strHomeYellowCards: String? = null,

        @SerializedName("strAwayYellowCards")
        val strAwayYellowCards: String? = null,

        @SerializedName("strHomeLineupGoalkeeper")
        val strHomeLineupGoalkeeper: String? = null,

        @SerializedName("strAwayLineupGoalkeeper")
        val strAwayLineupGoalkeeper: String? = null,

        @SerializedName("strHomeLineupDefense")
        val strHomeLineupDefense: String? = null,

        @SerializedName("strAwayLineupDefense")
        val strAwayLineupDefense: String? = null,

        @SerializedName("strHomeLineupMidfield")
        val strHomeLineupMidfield: String? = null,

        @SerializedName("strAwayLineupMidfield")
        val strAwayLineupMidfield: String? = null,

        @SerializedName("strHomeLineupForward")
        val strHomeLineupForward: String? = null,

        @SerializedName("strAwayLineupForward")
        val strAwayLineupForward: String? = null,

        @SerializedName("strHomeLineupSubstitutes")
        val strHomeLineupSubstitutes: String? = null,

        @SerializedName("strAwayLineupSubstitutes")
        val strAwayLineupSubstitutes: String? = null,

        @SerializedName("strHomeFormation")
        val strHomeFormation: String? = null,

        @SerializedName("strAwayFormation")
        val strAwayFormation: String? = null,

        @SerializedName("intHomeShots")
        val intHomeShots: String? = null,

        @SerializedName("intAwayShots")
        val intAwayShots: String? = null,

        @SerializedName("strDate")
        val strDate: String? = null,

        @SerializedName("strTime")
        val strTime: String? = null
)