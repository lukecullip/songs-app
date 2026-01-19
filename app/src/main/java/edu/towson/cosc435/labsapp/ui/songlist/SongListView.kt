package edu.towson.cosc435.labsapp.ui.songlist

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import edu.towson.cosc435.labsapp.data.model.Song
import edu.towson.cosc435.labsapp.ui.SongRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalFoundationApi
@Composable
fun SongListView(
    songs: List<Song>,
    selectedSong: Song? = null,
    onDelete: () -> Unit,
    onToggle: (Song) -> Unit,
    onSelectSong: (Song) -> Unit,
    waiting: Boolean = false,
    dialog: SongListViewModel.DeleteDialog,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        if(dialog.showDialog.value) {
            DeleteDialog(
                onDismiss = dialog::hideDialog,
                onDelete = onDelete
            )
        }
        val alpha = when(waiting) {
            true -> 0.2f
            false -> 1.0f
        }
        val configuration = LocalConfiguration.current
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // landscape
            Landscape(
                songs = songs,
                selectedSong = selectedSong,
                onDelete = dialog::showDialog,
                onToggle = onToggle,
                onSelectSong = onSelectSong,
                modifier = modifier.alpha(alpha)
            )
        } else {
            // portrait
            Portrait(
                songs = songs,
                onDelete = dialog::showDialog,
                onToggle = onToggle,
                onSelectSong = onSelectSong,
                modifier = modifier.alpha(alpha)
            )
        }
        if(waiting) {
            CircularProgressIndicator()
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DeleteDialog(
    onDismiss: () -> Unit,
    onDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = { TextButton(onClick = { onDelete(); onDismiss() }) { Text("Delete") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel")} },
        text = {
            Text("Are you sure?", style = MaterialTheme.typography.titleLarge)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SongListUI(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onDelete: (Song) -> Unit,
    onToggle: (Song) -> Unit,
    onSelectSong: (Song) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(songs) { song ->
            SongRow(song, onDelete, onToggle, onSelectSong)
        }
    }
}

@Composable
fun Portrait(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    onDelete: (Song) -> Unit,
    onToggle: (Song) -> Unit,
    onSelectSong: (Song) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        SongListUI(
            songs = songs,
            onDelete = onDelete,
            onToggle = onToggle,
            onSelectSong = onSelectSong
        )
    }
}

@Composable
fun Landscape(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    selectedSong: Song? = null,
    onDelete: (Song) -> Unit,
    onToggle: (Song) -> Unit,
    onSelectSong: (Song) -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Card(
            modifier = Modifier.weight(1.0f).padding(16.dp),

            ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(selectedSong?.name ?: "")
            }
        }
        SongListUI(
            modifier = Modifier.weight(1.0f),
            songs = songs,
            onDelete = onDelete,
            onToggle = onToggle,
            onSelectSong = onSelectSong
        )
    }
}