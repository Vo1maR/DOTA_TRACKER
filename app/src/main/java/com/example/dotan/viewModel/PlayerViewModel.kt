package com.example.dotan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotan.FavoriteAccount
import com.example.dotan.HeroInfo
import com.example.dotan.MatchDetailsResponse
import com.example.dotan.PlayerMatch
import com.example.dotan.PlayerResponse
import com.example.dotan.PlayerWinLossResponse
import com.example.dotan.repository.FavoriteAccountDao
import com.example.dotan.repository.OpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: OpenDotaRepository,
    private val favoriteAccountDao: FavoriteAccountDao) : ViewModel() {

    private val _playerInfo = MutableStateFlow<PlayerResponse?>(null)
    val playerInfo = _playerInfo.asStateFlow()

    private val _winLossInfo = MutableStateFlow(PlayerWinLossResponse(0, 0))
    val winLossInfo = _winLossInfo.asStateFlow()

    private val _matches = MutableStateFlow(emptyList<PlayerMatch>())
    val matches = _matches.asStateFlow()

    private val _matchDetails = MutableStateFlow<MatchDetailsResponse?>(null)
    val matchDetails = _matchDetails.asStateFlow()

    private val _heroesInfo = MutableStateFlow<Map<Int, HeroInfo>>(emptyMap())
    val heroesInfo = _heroesInfo.asStateFlow()

    private val _favoriteAccounts = MutableStateFlow(emptyList<FavoriteAccount>())
    val favoriteAccounts = _favoriteAccounts.asStateFlow()

    init {
        // Fetch favorite accounts when ViewModel is initialized
        getFavoriteAccounts()
    }
    fun getPlayerData(accountId: Int) {
        viewModelScope.launch {
            try {
                _playerInfo.value = repository.getPlayerInfo(accountId)
            } catch (e: Exception) {
                // Handle error
                println("Error fetching player info: ${e.message}")
            }
        }
    }

    fun getPlayerWinLoss(accountId: Int) {
        viewModelScope.launch {
            try {
                _winLossInfo.value = repository.getPlayerWinLoss(accountId)
            } catch (e: Exception) {
                // Handle error
                println("Error fetching win/loss info: ${e.message}")
            }
        }
    }

    fun getPlayerMatches(accountId: Int) {
        viewModelScope.launch {
            try {
                _matches.value = repository.getPlayerMatches(accountId)
            } catch (e: Exception) {
                // Handle error
                println("Error fetching matches: ${e.message}")
            }
        }
    }

    fun getMatchDetails(matchId: Long) {
        viewModelScope.launch {
            try {
                _matchDetails.value = repository.getMatchDetails(matchId)
            } catch (e: Exception) {
                // Handle error
                println("Error fetching match details: ${e.message}")
            }
        }
    }

    fun getHeroes() {
        viewModelScope.launch {
            try {
                val heroData = try {
                    repository.getHeroes()
                } catch (e: Exception) {
                    emptyList()
                }
                _heroesInfo.value = heroData.associateBy(
                    { it.id },
                    {
                        HeroInfo(
                            it.id,
                            localized_name = it.localized_name,
                            primary_attr = it.primary_attr,
                            img = "https://cdn.dota2.com${it.img}"
                        )
                    }
                )
            } catch (e: Exception) {
                // Handle error
                println("Error fetching match details: ${e.message}")
            }
        }
    }

    fun addFavoriteAccount(account_id: Int, personaname: String?, avatar: String?) {
        viewModelScope.launch {
            favoriteAccountDao.insertFavoriteAccount(
                FavoriteAccount(
                    account_id,
                    personaname,
                    avatar
                )
            )
        }
    }

    fun deleteFavoriteAccount(account_id: Int, personaname: String?, avatar: String?) {
        viewModelScope.launch {
            favoriteAccountDao.deleteFavoriteAccount(
                FavoriteAccount(
                    account_id,
                    personaname,
                    avatar
                )
            )
        }
    }

    fun getFavoriteAccounts() {
        viewModelScope.launch {
            favoriteAccountDao.getAllFavoriteAccounts().collect { accounts ->
                _favoriteAccounts.value = accounts
            }
        }
    }
}