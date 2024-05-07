package com.example.dotan

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenDotaService {
    @GET("players/{account_id}")
    suspend fun getPlayerInfo(@Path("account_id") accountId: Int): PlayerResponse

    @GET("players/{account_id}/wl")
    suspend fun getPlayerWinLoss(@Path("account_id") accountId: Int, @Query("limit") limit: Int = 20): PlayerWinLossResponse

    @GET("players/{account_id}/matches")
    suspend fun getPlayerMatches(@Path("account_id") accountId: Int, @Query("limit") limit: Int = 20): List<PlayerMatch>

    @GET("matches/{match_id}")
    suspend fun getMatchDetails(@Path("match_id") matchId: Long): MatchDetailsResponse

    @GET("heroStats")
    suspend fun getHeroes(): List<HeroInfo>

    @GET("players/{account_id}")
    suspend fun getPlayerSummary(@Path("account_id") accountId: Int): PlayerSummaryResponse
}
