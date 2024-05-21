package com.example.dotan

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.dotan.viewModel.PlayerViewModel

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
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            item {
                MatchInfoCard("Match ID", "${matchDetails?.match_id}")
                val radiantWin = matchDetails?.radiant_win ?: false
                MatchInfoCard("Winning Team", if (radiantWin) "Radiant" else "Dire")
                val durationSeconds = matchDetails?.duration ?: 0
                val durationMinutes = durationSeconds / 60
                val remainingSeconds = durationSeconds % 60
                MatchInfoCard("Duration", "$durationMinutes min $remainingSeconds sec")

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Player Name", style = TextStyle(fontSize = 16.sp), modifier = Modifier.weight(2f))
                    Text("Hero", style = TextStyle(fontSize = 16.sp), modifier = Modifier.weight(2f))
                    Text("K/D/A", style = TextStyle(fontSize = 16.sp), modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Radiant header
                Text(
                    "Radiant",
                    style = TextStyle(fontSize = 20.sp, color = Color.Green, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }

            items(matchDetails.players.take(5)) { player ->
                val heroInfo: HeroInfo? = heroMap[player.hero_id]
                PlayerInfoCard(
                    player.personaname ?: "Unknown",
                    heroInfo?.localized_name ?: "",
                    heroInfo?.img ?: "",
                    "${player.kills}/${player.deaths}/${player.assists}"
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))

                // Dire header
                Text(
                    "Dire",
                    style = TextStyle(fontSize = 20.sp, color = Color.Red, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    textAlign = TextAlign.Center
                )
            }

            items(matchDetails.players.drop(5)) { player ->
                val heroInfo: HeroInfo? = heroMap[player.hero_id]
                PlayerInfoCard(
                    player.personaname ?: "Unknown",
                    heroInfo?.localized_name ?: "",
                    heroInfo?.img ?: "",
                    "${player.kills}/${player.deaths}/${player.assists}"
                )
            }
        }
    } else {
        Text("Failed to load match details")
    }
}

@Composable
fun MatchInfoCard(title: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                title,
                style = TextStyle(fontSize = 16.sp, color = Color.Gray),
                modifier = Modifier.padding(bottom = 2.dp)
            )
            Text(value, style = TextStyle(fontSize = 16.sp))
        }
    }
}

@Composable
fun PlayerInfoCard(playerName: String, heroName: String, heroImg: String, kda: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(2f).padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(playerName, style = TextStyle(fontSize = 14.sp))
            }
            Column(
                modifier = Modifier.weight(2f).padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (heroImg.isNotEmpty()) {
                    Image(
                        painter = rememberImagePainter(heroImg),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp).padding(bottom = 4.dp)
                    )
                }
                Text(heroName, style = TextStyle(fontSize = 14.sp))
            }
            Text(
                kda,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.weight(1f).padding(4.dp)
            )
        }
    }
}
