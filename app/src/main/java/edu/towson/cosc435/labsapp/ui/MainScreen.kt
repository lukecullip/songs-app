package edu.towson.cosc435.labsapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.towson.cosc435.labsapp.ui.nav.AddSong
import edu.towson.cosc435.labsapp.ui.nav.SongList
import edu.towson.cosc435.labsapp.ui.nav.SongsNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MainScreen() {
    val nav = rememberNavController()
    Scaffold(
        topBar = {
            TopBar()
        },
        bottomBar = {
            BottomBar(nav = nav)
        }
    ) { pv: PaddingValues ->
        SongsNavGraph(nav, modifier = Modifier.padding(pv))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = { Text("Songs App") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        )
    )
}

@Composable
private fun BottomBar(
    nav: NavHostController 
) {
    val backStackEntry by nav.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    NavigationBar() {
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == SongList::class.java.canonicalName } == true,
            onClick = {
                nav.navigate(SongList) {
                    launchSingleTop = true
                    popUpTo(SongList)
                }
            },
            icon = {
                Icon(Icons.Default.Home, "")
            },
            label = {
                Text("SongList")
            }
        )
        NavigationBarItem(
            selected = currentDestination?.hierarchy?.any { it.route == AddSong::class.java.canonicalName } == true,
            onClick = {
                nav.navigate(AddSong) {
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(Icons.Default.Add, "")
            },
            label = {
                Text("New Song")
            }
        )
    }
}