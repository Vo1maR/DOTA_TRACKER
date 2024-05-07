package com.example.dotan

import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import openDotaService

@Composable
fun PlayerSearchScreen(navController: NavHostController) {
    var playerIdText by remember { mutableStateOf("") }
    val sharedPreferences = LocalContext.current.getSharedPreferences("favorites", Context.MODE_PRIVATE)
    val favoriteAccountIds = sharedPreferences.all.filterKeys { it.startsWith("favorite_account_id_") }
        .mapNotNull { it.value as? Int }

    Log.d("FavIds", favoriteAccountIds.toString())
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = playerIdText,
                onValueChange = { playerIdText = it },
                label = { Text("Enter Player ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("player_info/${playerIdText}")
                },
                enabled = playerIdText.isNotBlank()
            ) {
                Text("Search")
            }
        }
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            itemsIndexed(favoriteAccountIds) { index, it ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("player_info/${it}") }
                        .padding(16.dp)
                ) {
                    Text(text = "Favorite: ${it}") // Display player name
                }
            }
        }
    }
}