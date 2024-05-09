package com.example.dotan.repository

import com.example.dotan.MatchDetailsResponse
import com.example.dotan.OpenDotaService
import com.example.dotan.PlayerMatch
import com.example.dotan.PlayerResponse
import com.example.dotan.PlayerWinLossResponse
import javax.inject.Inject


class OpenDotaRepositoryImpl @Inject constructor(
    private val openDotaService: OpenDotaService) : OpenDotaRepository {

    override suspend fun getPlayerInfo(accountId: Int): PlayerResponse {
        return openDotaService.getPlayerInfo(accountId)
    }

    override suspend fun getPlayerWinLoss(accountId: Int, limit: Int): PlayerWinLossResponse {
        return openDotaService.getPlayerWinLoss(accountId, limit)
    }

    override suspend fun getPlayerMatches(accountId: Int, limit: Int): List<PlayerMatch> {
        return openDotaService.getPlayerMatches(accountId, limit)
    }

    override suspend fun getMatchDetails(matchId: Long): MatchDetailsResponse {
        return openDotaService.getMatchDetails(matchId)
    }

}