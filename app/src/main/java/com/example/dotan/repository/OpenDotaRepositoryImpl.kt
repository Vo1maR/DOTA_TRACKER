package com.example.dotan.repository

import javax.inject.Inject


class OpenDotaRepositoryImpl @Inject constructor(
    private val openDotaService: OpenDotaService
) : OpenDotaRepository {

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

    override suspend fun getHeroes(): List<HeroInfo> {
        return openDotaService.getHeroes()
    }
}