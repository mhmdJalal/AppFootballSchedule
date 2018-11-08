package com.mahlultech.footballclubs3.model

import com.google.gson.annotations.SerializedName

data class LeagueResponse(
        @SerializedName("leagues") val leagues: List<League>
)

data class League(
        @SerializedName("idLeague")
        val idLeague: String,

        @SerializedName("strLeague")
        val strLeague: String,

        @SerializedName("strSport")
        val strSport: String? = null
)