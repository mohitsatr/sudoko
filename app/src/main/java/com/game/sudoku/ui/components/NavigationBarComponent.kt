package com.game.sudoku.ui.components

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.utils.currentDestinationAsState
import com.ramcosta.composedestinations.utils.startDestination
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun NavigationBarComponent(
    navController: NavHostController,
    isVisible: Boolean,
    updateAvailable: Boolean
) {

    val directions = listOf(
//        NavigationBarDestination.Statistics,
        NavigationBarDestination.Home,
//        NavigationBarDestination.More
    )


    val currentDestination = navController.currentDestinationAsState().value
        ?: NavGraphs.root.defaultStartDirection

    if (isVisible) {
        NavigationBar {
            directions.forEach { destination ->
                NavigationBarItem(
                    icon = {
                        // change from original
                        if (updateAvailable) {
                            BadgedBox(
                                badge = {
                                    Badge()
                                }
                            ) {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = null
                                )
                            }
                        } else {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = null
                            )
                        }
                    },
                    selected = currentDestination == destination.direction,
                    label = {
                        Text(
                            text = stringResource(destination.label),
                            fontWeight = FontWeight.Bold
                        )
                    },
                    onClick = {
                        navController.toDestinationsNavigator().navigate(destination.direction) {
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
