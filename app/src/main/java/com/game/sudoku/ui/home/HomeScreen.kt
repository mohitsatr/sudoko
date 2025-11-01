package com.game.sudoku.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
//import com.game.sudoku.ui.game.components.AnimatedNavigation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.generated.destinations.GameScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
//
@Destination<RootGraph>(start = true)
@Composable
fun HomeScreen(
    viewMode: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Button(
                onClick = {
                    viewMode.startGame()
                    navigator.navigate(GameScreenDestination(
                        gameUid = viewMode.insertedBoardUid,
                        playedBefore = false,
                    )
                    )
                }
            ) {
                Text("Start Game")
            }
        }
    }
}
