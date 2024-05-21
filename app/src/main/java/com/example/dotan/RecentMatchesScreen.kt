package com.example.dotan

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.dotan.viewModel.PlayerViewModel
import openDotaService

@Composable
fun RecentMatchesScreen(navController: NavHostController, accountId: String?) {

    val viewModel: PlayerViewModel = hiltViewModel()
    LaunchedEffect(accountId) {
        viewModel.getPlayerMatches(accountId!!.toInt())
    }

    val matches by remember(viewModel.matches) { viewModel.matches }.collectAsState()

    if (matches.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Подписи к колонкам
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextWithLine("Hero", 20)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextWithLine("Stats(k/d/a)", 20)
                    }
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        TextWithLine("Result", 20)
                    }
                }
            }
            items(matches) { match ->
                MatchItem(match, navController)
            }
        }
    }
}

@Composable
fun TextWithLine(text: String, fontSize: Int) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Black)
        )
    }
}

@Composable
fun MatchItem(match: PlayerMatch, navController: NavHostController) {
    val heroInfo: HeroInfo? = heroMap.get(match.hero_id)
    val imageUri: String? = heroInfo?.img

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("match_details/${match.match_id}") }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(data = uri),
                    contentDescription = "Hero img",
                    modifier = Modifier.size(64.dp)
                )
            }
            Text(
                text = heroInfo?.localized_name ?: "",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${match.kills}/${match.deaths}/${match.assists}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val resultText = if (match.radiant_win == (match.player_slot < 5)) "Win" else "Lose"
            Text(
                text = resultText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (resultText == "Win") Color.Green else Color.Red,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
