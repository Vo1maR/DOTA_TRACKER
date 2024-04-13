package com.example.dotan

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import openDotaService

@Composable
fun RecentMatchesScreen(navController: NavHostController, accountId: String?) {
    var matches by remember { mutableStateOf(emptyList<PlayerMatch>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = accountId) {
        if (accountId != null) {
            try {
                matches = openDotaService.getPlayerMatches(accountId.toInt())
                isLoading = false
            } catch (e: Exception) {
                // Handle error
                println("Error fetching matches: ${e.message}")
            }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn {
            items(matches) { match ->
                MatchItem(match, navController)
            }
        }
    }
}

@Composable
fun MatchItem(match: PlayerMatch, navController: NavHostController) {
    val heroInfo: HeroInfo? = heroMap.get(match.hero_id)
    val imageUri: String? = heroInfo?.img
    println(imageUri)
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { navController.navigate("match_details/${match.match_id}") }
        .padding(16.dp)
    ) {
        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(
                    data = uri,
                    builder = {
                        crossfade(true)
                    }
                ),
                contentDescription = "Hero img",
                modifier = Modifier.size(48.dp) // Adjust size as needed
            )
        }
        Text(text = "${heroInfo?.localized_name}   ${match.kills}/${match.deaths}/${match.assists} ${if (match.radiant_win == (match.player_slot < 5)) "Win" else "Lose"}") // Display hero name
    }
}