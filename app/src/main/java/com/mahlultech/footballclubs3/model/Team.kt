package com.mahlultech.footballclubs3.model

import com.google.gson.annotations.SerializedName

data class TeamsResponse(
    @SerializedName("teams") val teams: List<Teams>
)

data class Teams(
    @SerializedName("idTeam") val idTeam: String,
    @SerializedName("idSoccerXML") val idSoccerXML: String,
    @SerializedName("intLoved") val intLoved: Any,
    @SerializedName("strTeam") val strTeam: String,
    @SerializedName("strTeamShort") val strTeamShort: Any,
    @SerializedName("strAlternate") val strAlternate: String,
    @SerializedName("intFormedYear") val intFormedYear: String,
    @SerializedName("strSport") val strSport: String,
    @SerializedName("strLeague") val strLeague: String,
    @SerializedName("idLeague") val idLeague: String,
    @SerializedName("strDivision") val strDivision: Any,
    @SerializedName("strManager") val strManager: String,
    @SerializedName("strStadium") val strStadium: String,
    @SerializedName("strKeywords") val strKeywords: String,
    @SerializedName("strRSS") val strRSS: String,
    @SerializedName("strStadiumThumb") val strStadiumThumb: String,
    @SerializedName("strStadiumDescription") val strStadiumDescription: String,
    @SerializedName("strStadiumLocation") val strStadiumLocation: String,
    @SerializedName("intStadiumCapacity") val intStadiumCapacity: String,
    @SerializedName("strWebsite") val strWebsite: String,
    @SerializedName("strFacebook") val strFacebook: String,
    @SerializedName("strTwitter") val strTwitter: String,
    @SerializedName("strInstagram") val strInstagram: String,
    @SerializedName("strDescriptionEN") val strDescriptionEN: String,
    @SerializedName("strGender") val strGender: String,
    @SerializedName("strCountry") val strCountry: String,
    @SerializedName("strTeamBadge") val strTeamBadge: String,
    @SerializedName("strTeamJersey") val strTeamJersey: String,
    @SerializedName("strTeamLogo") val strTeamLogo: String,
    @SerializedName("strTeamFanart1") val strTeamFanart1: String,
    @SerializedName("strTeamFanart2") val strTeamFanart2: String,
    @SerializedName("strTeamFanart3") val strTeamFanart3: String,
    @SerializedName("strTeamFanart4") val strTeamFanart4: String,
    @SerializedName("strTeamBanner") val strTeamBanner: String
)

data class HomeTeamResponse(
    @SerializedName("teams") val teams: List<HomeTeam>
)

data class HomeTeam(
        @SerializedName("idTeam")
        var teamId: String? = null,

        @SerializedName("strTeam")
        var teamName: String? = null,

        @SerializedName("strTeamBadge")
        var teamBadge: String? = null,

        @SerializedName("intFormedYear")
        var teamFormedYear: String? = null,

        @SerializedName("strStadium")
        var teamStadium: String? = null,

        @SerializedName("strDescriptionEN")
        var teamDescription: String? = null
)

data class AwayTeamResponse(
        @SerializedName("teams") val teams: List<AwayTeam>
)

data class AwayTeam(
        @SerializedName("idTeam")
        var teamId: String? = null,

        @SerializedName("strTeam")
        var teamName: String? = null,

        @SerializedName("strTeamBadge")
        var teamBadge: String? = null,

        @SerializedName("intFormedYear")
        var teamFormedYear: String? = null,

        @SerializedName("strStadium")
        var teamStadium: String? = null,

        @SerializedName("strDescriptionEN")
        var teamDescription: String? = null
)