package com.example.dotan

import android.content.Context
import android.graphics.Picture
import android.net.Uri
import android.util.Log
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.android.volley.toolbox.ImageRequest
import com.example.dotan.viewModel.PlayerViewModel
import kotlinx.coroutines.launch
import openDotaService

@Composable
fun PlayerInfoScreen(navController: NavHostController, accountId: String?) {
    val viewModel: PlayerViewModel = hiltViewModel()
    LaunchedEffect(accountId) {
        viewModel.getPlayerData(accountId!!.toInt())
        viewModel.getPlayerWinLoss(accountId!!.toInt())
    }

    Log.d("player", viewModel.getPlayerData(accountId!!.toInt()).toString())
    val playerInfo = viewModel.playerInfo.collectAsState().value
    val imageUri = remember(playerInfo?.profile?.avatarfull) {
        playerInfo?.profile?.avatarfull?.let { Uri.parse(it) }
    }
    var isLoading by remember { mutableStateOf(true) }

    var winLossInfo = viewModel.winLossInfo.collectAsState().value
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
            modifier = Modifier.padding(16.dp)
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
                    modifier = Modifier.size(128.dp) // Adjust size as needed
                )
            }
            Button(onClick = {
                isFavorite = !isFavorite

                if (isFavorite && accountId != null && playerInfo != null) {
                    viewModel.addFavoriteAccount(playerInfo.profile.account_id,
                        playerInfo?.profile?.personaname,
                        playerInfo?.profile?.avatar)
                    Log.d("EditorValueAdded", playerInfo!!.profile.account_id.toString())
                    playerInfo!!.profile.personaname?.let { Log.d("id", it) }
                } else {
                    viewModel.deleteFavoriteAccount(playerInfo.profile.account_id,
                        playerInfo?.profile?.personaname,
                        playerInfo?.profile?.avatar)
                    Log.d("EditorValueRemoved", "0")
                }
            }) {
                Text(if (isFavorite) "Remove from Favorites" else "Add to Favorites")
            }
            Text("Account ID: ${playerInfo?.profile?.account_id}")
            Text("Player Name: ${playerInfo?.profile?.personaname ?: "Unknown"}")
            Text("Country: ${playerInfo?.profile?.loccountrycode ?: "Unknown"}")
            Text("Rank: ${playerInfo?.rank_tier ?: "Unknown"}")
            winLossInfo?.let {
                Text("Wins: ${it.win}, Losses: ${it.lose}")
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate("recent_matches/$accountId")
            }) {
                Text("View Recent Matches")
            }
        }
    } else {
        // Display error message if playerInfo is null
        Text("Error: Player information not found")
    }
}