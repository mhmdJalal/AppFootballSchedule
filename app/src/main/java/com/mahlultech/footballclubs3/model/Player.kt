package com.mahlultech.footballclubs3.model
import com.google.gson.annotations.SerializedName

data class PlayerResponse(
    @SerializedName("player") val players: List<Player>
)

data class PlayerDetailResponse(
        @SerializedName("players") val players: List<Player>
)

data class Player(
    @SerializedName("idPlayer") val idPlayer: String? = null,
    @SerializedName("idTeam") val idTeam: String? = null,
    @SerializedName("strPlayer") val strPlayer: String? = null,
    @SerializedName("strTeam") val strTeam: String? = null,
    @SerializedName("strSport") val strSport: String? = null,
    @SerializedName("strDescriptionEN") val strDescriptionEN: String? = null,
    @SerializedName("strPosition") val strPosition: String? = null,
    @SerializedName("strHeight") val strHeight: String? = null,
    @SerializedName("strWeight") val strWeight: String? = null,
    @SerializedName("strCutout") val strCutout: String? = null,
    @SerializedName("strFanart1") val strFanart1: String? = null,
    @SerializedName("strFanart2") val strFanart2: String? = null,
    @SerializedName("strFanart3") val strFanart3: String? = null,
    @SerializedName("strFanart4") val strFanart4: String? = null
)