package com.example.sports.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sports.presentation.insertperformance.InsertPerformanceScreen
import com.example.sports.presentation.insertperformance.InsertPerformanceViewModel
import com.example.sports.presentation.performancelist.ListPerformancesEvent
import com.example.sports.presentation.performancelist.ListPerformancesScreen
import com.example.sports.presentation.performancelist.ListPerformancesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppNavigation() {
    val insertVm: InsertPerformanceViewModel = koinViewModel()
    val listVm: ListPerformancesViewModel = koinViewModel()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIST_PERFORMANCES) {
        composable(Routes.LIST_PERFORMANCES) {
            ListPerformancesScreen(
                listVm.uiState,
                onNavigateToInsert = { navController.navigate(Routes.INSERT_PERFORMANCE) },
                onEvent = listVm::onEvent,
            )
        }
        composable(Routes.INSERT_PERFORMANCE) {
            InsertPerformanceScreen(
                insertVm.uiState,
                onEvent = insertVm::onEvent,
                onSaved = {
                    navController.popBackStack()
                    listVm.onEvent(ListPerformancesEvent.Refresh)
                }
            )
        }
    }
}
