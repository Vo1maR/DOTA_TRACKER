package com.example.dotan

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import openDotaService

data class Player(
    val account_id: Int,
    val steamid: String?,
    val avatar: String?,
    val avatarmedium: String?,
    val avatarfull: String?,
    val profileurl: String?,
    val personaname: String?,
    val last_login: String?,
    val full_history_time: String?,
    val cheese: Int?,
    val fh_unavailable: Boolean,
    val loccountrycode: String?,
    val name: String?,
    val country_code: String?,
    val fantasy_role: Int,
    val team_id: Int?,
    val team_name: String?,
    val team_tag: String?,
    val is_locked: Boolean,
    val is_pro: Boolean,
    val locked_until: Int?
)

data class PlayerResponse(
    val solo_competitive_rank: Int?,
    val competitive_rank: Int?,
    val rank_tier: Int?,
    val leaderboard_rank: Int?,
    val profile: Player,
)

data class PlayerWinLossResponse(
    val win: Int,
    val lose: Int
)

data class PlayerMatch(
    val match_id: Long,
    val player_slot: Int,
    val radiant_win: Boolean,
    val duration: Int,
    val game_mode: Int,
    val lobby_type: Int,
    val hero_id: Int,
    val start_time: Long,
    val kills: Int,
    val deaths: Int,
    val assists: Int
)

data class MatchDetailsResponse(
    val match_id: Long,
    val radiant_win: Boolean,
    val duration: Int,
    val start_time: Long,
    val game_mode: Int,
    val lobby_type: Int,
    val radiant_team_id: Int,
    val dire_team_id: Int,
    val radiant_name: String?,
    val dire_name: String?,
    val leagueid: Int?,
    val league_name: String?,
    val radiant_score: Int,
    val dire_score: Int,
    val players: List<PlayerDetails>
)

data class PlayerDetails(
    val account_id: Int?,
    val player_slot: Int,
    val hero_id: Int,
    val item_0: Int,
    val item_1: Int,
    val item_2: Int,
    val item_3: Int,
    val item_4: Int,
    val item_5: Int,
    val kills: Int,
    val deaths: Int,
    val assists: Int,
    val gold: Int,
    val last_hits: Int,
    val denies: Int,
    val gold_per_min: Int,
    val xp_per_min: Int,
    val hero_damage: Int,
    val hero_healing: Int,
    val tower_damage: Int,
    val level: Int,
    val personaname: String?
)

data class Team(
    val team_id: Int,
    val name: String?,
    val tag: String?,
    val logo_url: String?
)

data class PicksBans(
    val is_pick: Boolean,
    val hero_id: Int,
    val team: Int,
    val order: Int
)

data class Objectives(
    val a: Int
)

data class HeroInfo(
    val id: Int,
    val localized_name: String,
    val primary_attr: String,
    val img: String
)
data class PlayerProfile(val accountId: Int, val name: String)
data class PlayerSummaryResponse(
    val profile: Player // Assuming Player data class from previous steps
)
suspend fun buildHeroMap(): Map<Int, HeroInfo>
{
    val heroData = try {
        openDotaService.getHeroes()
    } catch (e: Exception) {
        emptyList()
    }
    return heroData.associateBy(
        { it.id },
        { HeroInfo(it.id, localized_name = it.localized_name, primary_attr = it.primary_attr, img = "https://cdn.dota2.com${it.img}") }
    )
}



