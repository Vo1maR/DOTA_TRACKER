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
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.android.volley.toolbox.ImageRequest
import kotlinx.coroutines.launch
import openDotaService

@Composable
fun PlayerInfoScreen(navController: NavHostController, accountId: String?) {
    var playerInfo by remember { mutableStateOf<PlayerResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val imageUri = remember(playerInfo?.profile?.avatar) {
        playerInfo?.profile?.avatarfull?.let { Uri.parse(it) }
    }
    var winLossInfo by remember { mutableStateOf<PlayerWinLossResponse?>(null) }
    val sharedPreferences = LocalContext.current.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    var isFavorite by remember { mutableStateOf(sharedPreferences.contains("favorite_account_id_${accountId}")) }

    LaunchedEffect(key1 = accountId) {
        if (accountId != null) {
            try {
                playerInfo = openDotaService.getPlayerInfo(accountId.toInt())
                winLossInfo = openDotaService.getPlayerWinLoss(accountId.toInt())
                isLoading = false

                // Initialize isFavorite state here after fetching data
                isFavorite = sharedPreferences.contains("favorite_account_id_${playerInfo!!.profile.account_id}")
            } catch (e: Exception) {
                println("Error fetching player info: ${e.message}")
                isLoading = false
            }
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (playerInfo != null) {
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
                val editor = sharedPreferences.edit()
                if (isFavorite && accountId != null && playerInfo != null) {
                    editor.putInt("favorite_account_id_${playerInfo!!.profile.account_id}",
                        playerInfo!!.profile.account_id
                    )
                    Log.d("EditorValueAdded", playerInfo!!.profile.account_id.toString())
                    playerInfo!!.profile.personaname?.let { Log.d("id", it) }
                } else {
                    editor.remove("favorite_account_id_${playerInfo!!.profile.account_id}")
                    Log.d("EditorValueRemoved", "0")
                }
                editor.apply()
                Log.d("EditorValue", editor.toString())
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