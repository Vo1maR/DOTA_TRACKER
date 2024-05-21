package com.example.dotan

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.dotan.viewModels.PlayerViewModel

@Composable
fun PlayerSearchScreen(navController: NavHostController) {
    val viewModel: PlayerViewModel = hiltViewModel()
    var playerIdText by remember { mutableStateOf("") }
    val favoriteAccounts by viewModel.favoriteAccounts.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.dota),
            contentDescription = null,
            modifier = Modifier
                .size(240.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
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
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Favorite Players",
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.align(Alignment.Start).padding(start = 16.dp)
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            itemsIndexed(favoriteAccounts) { index, it ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { navController.navigate("player_info/${it.account_id}") },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Image(
                            painter = rememberImagePainter(data = it.avatar ?: ""),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(text = it.personaname ?: "Unknown Player", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Account ID: ${it.account_id}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
