package com.example.dotan.views

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dotan.viewModels.PlayerViewModel

@Composable
fun PlayerSearchScreen(navController: NavHostController) {
    val viewModel: PlayerViewModel = hiltViewModel()
    var playerIdText by remember { mutableStateOf("") }
    val favoriteAccounts by viewModel.favoriteAccounts.collectAsState(initial = emptyList())

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
        LazyColumn(modifier = Modifier.weight(1f)) {
            itemsIndexed(favoriteAccounts) { index, account ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("player_info/${account.account_id}") }
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween // Arrange items with space between
                ) {
                    Text(text = "${account.personaname}")
                    IconButton(onClick = {
                        // Call function to delete favorite account at index 'index'
                        viewModel.deleteFavoriteAccount(account.account_id,null,null)
                    }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}