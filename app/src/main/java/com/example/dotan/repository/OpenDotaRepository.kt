package com.example.dotan.repository

interface OpenDotaRepository {
    suspend fun getPlayerInfo(accountId: Int): PlayerResponse
    suspend fun getPlayerWinLoss(accountId: Int, limit: Int = 20): PlayerWinLossResponse
    suspend fun getPlayerMatches(accountId: Int, limit: Int = 20): List<PlayerMatch>
    suspend fun getMatchDetails(matchId: Long): MatchDetailsResponse
    suspend fun getHeroes(): List<HeroInfo>
}