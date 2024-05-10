package com.example.dotan.views

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dotan.repository.HeroInfo
import com.example.dotan.viewModels.PlayerViewModel

@Composable
fun MatchDetailsScreen(navController: NavHostController, matchId: Long?) {
    var isLoading by remember { mutableStateOf(false) }

    val viewModel: PlayerViewModel = hiltViewModel()
    LaunchedEffect(matchId) {
        viewModel.getMatchDetails(matchId!!)
        viewModel.getHeroes()
    }
    val heroMap = viewModel.heroesInfo.collectAsState().value
    val matchDetails = viewModel.matchDetails.collectAsState().value

    if (isLoading && matchDetails == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (matchDetails != null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Match ID: ${matchDetails?.match_id}")
            Text("Radiant Victory: ${matchDetails?.radiant_win}")
            Text("Duration: ${matchDetails?.duration} seconds")
            matchDetails?.players?.forEach { player ->
                val heroInfo: HeroInfo? = heroMap.get(player.hero_id)
                Text("${player.personaname ?: "Unknown"} , " +
                        "${heroInfo?.localized_name}, K/D/A: ${player.kills}/${player.deaths}/${player.assists}")
            }
        }
    } else {
        Text("Failed to load match details")
    }
}