package com.game.sudoku.ui.components

//@Composable
//fun NavigationBarComponent(
//    navController: NavHostController,
//    isVisible: Boolean,
//    updateAvailable: Boolean
//) {
//
//    val directions = listOf(
//        NavigationBarDestination.Statistics,
//        NavigationBarDestination.Home,
//        NavigationBarDestination.More
//    )


//    val currentDestination = navController.currentDestinationAsState().value
//        ?: NavGraphs.root.defaultStartDirection
//
//    if (isVisible) {
//        NavigationBar {
//            directions.forEach { destination ->
//                NavigationBarItem(
//                    icon = {
//                        if (updateAvailable) {
//                            BadgedBox(
//                                badge = {
//                                    Badge()
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = destination.icon,
//                                    contentDescription = null
//                                )
//                            }
//                        } else {
//                            Icon(
//                                imageVector = destination.icon,
//                                contentDescription = null
//                            )
//                        }
//                    },
//                    selected = currentDestination == destination.direction,
//                    label = {
//                        Text(
//                            text = stringResource(destination.label),
//                            fontWeight = FontWeight.Bold
//                        )
//                    },
//                    onClick = {
//                        navController.toDestinationsNavigator().navigate(destination.direction) {
//                            launchSingleTop = true
//                        }
//                    }
//                )
//            }
//        }
//    }
//}
//