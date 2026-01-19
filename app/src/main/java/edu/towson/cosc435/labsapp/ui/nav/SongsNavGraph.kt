package edu.towson.cosc435.labsapp.ui.nav

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ComponentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import edu.towson.cosc435.labsapp.MainActivity
import edu.towson.cosc435.labsapp.data.model.Song
import edu.towson.cosc435.labsapp.ui.newsong.NewSongView
import edu.towson.cosc435.labsapp.ui.songlist.SongListView
import edu.towson.cosc435.labsapp.ui.songlist.SongListViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalPermissionsApi::class)
@ExperimentalFoundationApi
@Composable
fun SongsNavGraph(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = SongList
    ) {
        composable<SongList> {
            val songsListViewModel: SongListViewModel = viewModel(viewModelStoreOwner = LocalContext.current as MainActivity)
            val songList by songsListViewModel.songs
            val selectedSong by songsListViewModel.selectedSong
            RequestLocationPermissions(vm = songsListViewModel)
            SongListView(
                songs = songList,
                selectedSong = selectedSong,
                onDelete = songsListViewModel::deleteSong,
                onToggle = songsListViewModel::toggleAwesome,
                onSelectSong = songsListViewModel::selectSong,
                waiting = songsListViewModel.waiting.value,
                dialog = songsListViewModel.dialog,
                modifier = modifier
            )
        }
        composable<AddSong> {
            val songsListViewModel: SongListViewModel = viewModel(viewModelStoreOwner = LocalContext.current as MainActivity)
            NewSongView(
                onAddSong = { song: Song ->
                    songsListViewModel.addSong(song)
                    navController.popBackStack() // DON'T call navigate!!
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@ExperimentalPermissionsApi
@Composable
fun RequestLocationPermissions(vm: SongListViewModel) {
    // TODO 1. - Request notification permissions and location permissions
    // Request location permissions
    val permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.POST_NOTIFICATIONS,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    permissionState.permissions.forEach { per: PermissionState ->
        when (per.status) {
            PermissionStatus.Granted -> {
                // TODO - Subscribe to location changes (hint: use the ViewModel)
            }
            is PermissionStatus.Denied -> {
                LaunchedEffect(key1 = true) {
                    per.launchPermissionRequest()
                }
            }
        }
    }
}