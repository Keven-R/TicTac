package com.cnc.tictac.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cnc.tictac.R
import com.cnc.tictac.ui.components.BackButton
import com.cnc.tictac.ui.components.TableCell
import com.cnc.tictac.ui.components.TableHeader
import com.cnc.tictac.viewmodel.TicTacViewModel

@Composable
fun Leaderboard(navController: NavHostController, viewModel: TicTacViewModel) {
    // CONTAINER: Set bg colour
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary)
    ) {
        // CONTAINER: All content on screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // CONTAINER: Top nav
            Row(modifier = Modifier.fillMaxWidth()) {
                // ELEMENT: Back button showing current page title "profile")
                BackButton(stringResource(id = R.string.button_leaderboard),navController)
            }

            // Required information for Leaderboard view
            val column1Weight = .1f
            val column2Weight = .45f
            val column3Weight = .1f
            val column4Weight = .1f
            val column5Weight = .1f
            val column6Weight = .1f
            val players = viewModel.leaderboard

            // LEADERBOARD view
            LazyColumn(Modifier.fillMaxWidth().weight(1f).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Header
                item {
                    Row() {
                        TableHeader(text = stringResource(id = R.string.header_rank), weight = column1Weight)
                        TableHeader(text = stringResource(id = R.string.header_user), weight = column2Weight)
                        TableHeader(text = stringResource(id = R.string.header_wins), weight = column3Weight)
                        TableHeader(text = stringResource(id = R.string.header_draw), weight = column4Weight)
                        TableHeader(text = stringResource(id = R.string.header_loss), weight = column5Weight)
                        TableHeader(text = stringResource(id = R.string.header_total), weight = column6Weight)
                    }
                }
                // All other items
                items(players.count()) { i ->
                    Row(Modifier.fillMaxWidth()) {
                        val stats = viewModel.getPlayerStats(players[i])
                        val wins = stats.first
                        val draws = stats.second
                        val losses = stats.third
                        val total = viewModel.getPlayerTotalGames(players[i])

                        TableCell(text = (i+1).toString(), weight = column1Weight)
                        TableCell(text = players[i].playerName, weight = column2Weight)
                        TableHeader(text = wins.toString(), weight = column3Weight)
                        TableHeader(text = draws.toString(), weight = column4Weight)
                        TableHeader(text = losses.toString(), weight = column5Weight)
                        TableHeader(text = total.toString(), weight = column6Weight)
                    }
                }
            }
        }
    }
}