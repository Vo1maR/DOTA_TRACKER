package com.example.dotan.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dotan.MatchDetailsResponse
import com.example.dotan.PlayerMatch
import com.example.dotan.PlayerResponse
import com.example.dotan.PlayerWinLossResponse
import com.example.dotan.repository.OpenDotaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerViewModel @Inject constructor(
    private val repository: OpenDotaRepository) : ViewModel() {

    private val _playerInfo = MutableStateFlow<PlayerResponse?>(null)
    val playerInfo = _playerInfo.asStateFlow()

    private val _winLossInfo = MutableStateFlow(PlayerWinLossResponse(0, 0))
    val winLossInfo= _winLossInfo.asStateFlow()

    private val _matches = MutableStateFlow(emptyList<PlayerMatch>())
    val matches = _matches.asStateFlow()

    private val _matchDetails = MutableStateFlow<MatchDetailsResponse?>(null)
    val matchDetails = _matchDetails.asStateFlow()

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

    // ... Add other functions for fetching and processing data as needed ...
}