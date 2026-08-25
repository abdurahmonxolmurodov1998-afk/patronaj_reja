package com.patronaj.reja.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import androidx.compose.foundation.layout.padding
import com.patronaj.reja.ui.screens.dashboard.DashboardScreen
import com.patronaj.reja.ui.screens.patient.PatientAddEditScreen
import com.patronaj.reja.ui.screens.patient.PatientDetailScreen
import com.patronaj.reja.ui.screens.patient.PatientListScreen
import com.patronaj.reja.ui.screens.standards.StandardAddEditScreen
import com.patronaj.reja.ui.screens.standards.StandardsScreen
import com.patronaj.reja.ui.screens.todayplan.TodayPlanScreen

sealed class Screen(val route: String, val label: String) {
    object Dashboard : Screen("dashboard", "Bosh sahifa")
    object Patients : Screen("patients", "Aholi")
    object TodayPlan : Screen("today_plan", "Bugungi reja")
    object Standards : Screen("standards", "Standartlar")

    object PatientAdd : Screen("patient_add", "Aholi qo'shish")
    object PatientEdit : Screen("patient_edit/{patientId}", "Tahrirlash") {
        fun createRoute(id: Long) = "patient_edit/$id"
    }
    object PatientDetail : Screen("patient_detail/{patientId}", "Aholi kartasi") {
        fun createRoute(id: Long) = "patient_detail/$id"
    }
    object StandardAdd : Screen("standard_add", "Yangi standart")
    object StandardEdit : Screen("standard_edit/{standardId}", "Standartni tahrirlash") {
        fun createRoute(id: Long) = "standard_edit/$id"
    }
}

private val bottomTabs = listOf(Screen.Dashboard, Screen.Patients, Screen.TodayPlan, Screen.Standards)

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { AppBottomBar(navController) }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = androidx.compose.ui.Modifier.padding(padding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(onGoToTodayPlan = {
                    navController.navigate(Screen.TodayPlan.route)
                })
            }
            composable(Screen.Patients.route) {
                PatientListScreen(
                    onAddClick = { navController.navigate(Screen.PatientAdd.route) },
                    onPatientClick = { id -> navController.navigate(Screen.PatientDetail.createRoute(id)) }
                )
            }
            composable(Screen.TodayPlan.route) {
                TodayPlanScreen(onPatientClick = { id -> navController.navigate(Screen.PatientDetail.createRoute(id)) })
            }
            composable(Screen.Standards.route) {
                StandardsScreen(
                    onAddClick = { navController.navigate(Screen.StandardAdd.route) },
                    onEditClick = { id -> navController.navigate(Screen.StandardEdit.createRoute(id)) }
                )
            }
            composable(Screen.PatientAdd.route) {
                PatientAddEditScreen(patientId = 0L, onDone = { navController.popBackStack() })
            }
            composable(
                Screen.PatientEdit.route,
                arguments = listOf(navArgument("patientId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("patientId") ?: 0L
                PatientAddEditScreen(patientId = id, onDone = { navController.popBackStack() })
            }
            composable(
                Screen.PatientDetail.route,
                arguments = listOf(navArgument("patientId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("patientId") ?: 0L
                PatientDetailScreen(
                    patientId = id,
                    onBack = { navController.popBackStack() },
                    onEdit = { pid -> navController.navigate(Screen.PatientEdit.createRoute(pid)) }
                )
            }
            composable(Screen.StandardAdd.route) {
                StandardAddEditScreen(standardId = 0L, onDone = { navController.popBackStack() })
            }
            composable(
                Screen.StandardEdit.route,
                arguments = listOf(navArgument("standardId") { type = NavType.LongType })
            ) { entry ->
                val id = entry.arguments?.getLong("standardId") ?: 0L
                StandardAddEditScreen(standardId = id, onDone = { navController.popBackStack() })
            }
        }
    }
}

@Composable
private fun AppBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationBar {
        val icons = mapOf(
            Screen.Dashboard.route to Icons.Default.Home,
            Screen.Patients.route to Icons.Default.People,
            Screen.TodayPlan.route to Icons.Default.CalendarToday,
            Screen.Standards.route to Icons.Default.Rule
        )
        bottomTabs.forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icons[screen.route]!!, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}
