package com.example.dotan

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.dotan.viewModel.PlayerViewModel

@Composable
fun PlayerInfoScreen(navController: NavHostController, accountId: String?) {
    val viewModel: PlayerViewModel = hiltViewModel()
    LaunchedEffect(accountId) {
        viewModel.getPlayerData(accountId!!.toInt())
        viewModel.getPlayerWinLoss(accountId.toInt())
    }

    Log.d("player", viewModel.getPlayerData(accountId!!.toInt()).toString())
    val playerInfo = viewModel.playerInfo.collectAsState().value
    val imageUri = remember(playerInfo?.profile?.avatarfull) {
        playerInfo?.profile?.avatarfull?.let { Uri.parse(it) }
    }
    var isLoading by remember { mutableStateOf(true) }

    val winLossInfo = viewModel.winLossInfo.collectAsState().value
    val sharedPreferences = LocalContext.current.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    var isFavorite by remember { mutableStateOf(sharedPreferences.contains("favorite_account_id_${accountId}")) }

    if (isLoading && playerInfo == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (playerInfo != null) {
        isLoading = false
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            imageUri?.let { uri ->
                Image(
                    painter = rememberImagePainter(
                        data = uri,
                        builder = {
                            crossfade(true)
                        }
                    ),
                    contentDescription = "Player Avatar",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 8.dp)
                )
            }
            Text(
                text = playerInfo.profile.personaname ?: "Unknown Player",
                style = TextStyle(fontSize = 40.sp),
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Account ID:",
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "${playerInfo.profile.account_id}",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Country:",
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = playerInfo.profile.loccountrycode ?: "Unknown",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "Rank:",
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = playerInfo.rank_tier?.toString() ?: "Unknown",
                        style = TextStyle(fontSize = 18.sp),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }
            winLossInfo?.let {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Wins:",
                                style = TextStyle(fontSize = 18.sp, color = Color.Gray)
                            )
                            Text(
                                text = "${it.win}",
                                style = TextStyle(fontSize = 18.sp)
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Losses:",
                                style = TextStyle(fontSize = 18.sp, color = Color.Gray)
                            )
                            Text(
                                text = "${it.lose}",
                                style = TextStyle(fontSize = 18.sp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    isFavorite = !isFavorite

                    if (isFavorite && accountId != null && playerInfo != null) {
                        viewModel.addFavoriteAccount(
                            playerInfo.profile.account_id,
                            playerInfo.profile.personaname,
                            playerInfo.profile.avatar
                        )
                        Log.d("EditorValueAdded", playerInfo.profile.account_id.toString())
                        playerInfo.profile.personaname?.let { Log.d("id", it) }
                    } else {
                        viewModel.deleteFavoriteAccount(
                            playerInfo.profile.account_id,
                            playerInfo.profile.personaname,
                            playerInfo.profile.avatar
                        )
                        Log.d("EditorValueRemoved", "0")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                    style = TextStyle(fontSize = 20.sp)
                )
            }

            Button(
                onClick = {
                    navController.navigate("recent_matches/$accountId")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "View Recent Matches",
                    style = TextStyle(fontSize = 20.sp)
                )
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Error: Player information not found", style = TextStyle(fontSize = 40.sp))
        }
    }
}