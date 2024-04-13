package com.example.dotan

import android.graphics.Picture
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.android.volley.toolbox.ImageRequest
import kotlinx.coroutines.launch
import openDotaService

@Composable
fun MatchDetailsScreen(navController: NavHostController, matchId: Long?) {
    var matchDetails by remember { mutableStateOf<MatchDetailsResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = matchId) {
        if (matchId != null) {
            isLoading = true
            try {
                matchDetails = openDotaService.getMatchDetails(matchId)
            } catch (e: Exception) {
                println("Error fetching match details: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading) {
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