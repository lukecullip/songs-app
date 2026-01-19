package edu.towson.cosc435.labsapp.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import edu.towson.cosc435.labsapp.data.model.Song
import kotlinx.coroutines.Dispatchers

@ExperimentalFoundationApi
@Composable
fun SongRow(
    song: Song,
    onDelete: (Song) -> Unit,
    onToggle: (Song) -> Unit,
    onSelectSong: (Song) -> Unit
) {
    Log.d("TAG", song.name)
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .padding(start=16.dp, end=16.dp, top=5.dp, bottom=5.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .combinedClickable(
                    onLongClick = { onDelete(song) }
                ) { onSelectSong(song) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier.weight(1.5f)
            ) {
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Name:", modifier = Modifier.weight(1.0f))
                    Text(song.name, modifier = Modifier.weight(2.0f), fontSize = 28.sp, color = MaterialTheme.colorScheme.secondary)
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Artist:", modifier = Modifier.weight(1.0f))
                    Text(song.artist, modifier = Modifier.weight(2.0f))
                }
                Row(
                    modifier = Modifier.padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Track:", modifier = Modifier.weight(1.0f))
                    Text(song.track.toString(), modifier = Modifier.weight(2.0f))
                }
            }
            Column(
                modifier = Modifier.weight(1.0f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(128.dp),
                    model = ImageRequest
                        .Builder(LocalContext.current)
                        .data(song.iconUrl)
                        .build()
                    ,
                    loading ={
                        CircularProgressIndicator()
                    },
                    contentDescription = null
                )
                Spacer(modifier = Modifier.padding(bottom=5.dp))
                Row() {
                    Checkbox(checked = song.isAwesome, onCheckedChange = { onToggle(song) }, modifier = Modifier.padding(end=5.dp))
                    Text("Is Awesome")
                }
            }
        }
    }
}
