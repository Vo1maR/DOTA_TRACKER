package com.example.dotan.repository

import com.example.dotan.MatchDetailsResponse
import com.example.dotan.PlayerMatch
import com.example.dotan.PlayerResponse
import com.example.dotan.PlayerWinLossResponse

interface OpenDotaRepository {
    suspend fun getPlayerInfo(accountId: Int): PlayerResponse
    suspend fun getPlayerWinLoss(accountId: Int, limit: Int = 20): PlayerWinLossResponse
    suspend fun getPlayerMatches(accountId: Int, limit: Int = 20): List<PlayerMatch>
    suspend fun getMatchDetails(matchId: Long): MatchDetailsResponse
}