package edu.towson.cosc435.labsapp.ui.newsong

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import edu.towson.cosc435.labsapp.data.model.Song
import edu.towson.cosc435.labsapp.ui.theme.LabsAppTheme
import java.lang.Exception

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun NewSongView(
    vm: NewSongViewModel = viewModel(),
    onAddSong: (Song) -> Unit
) {
    // for the Toast
    val ctx = LocalContext.current
    // to handle moving focus between TextFields
    val (nameTf, artistTf, trackTf) = remember { FocusRequester.createRefs() }
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(true) {
        nameTf.requestFocus()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            "New Song",
            fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .focusRequester(nameTf)
        )
        OutlinedTextField(
            value = vm.name.value,
            onValueChange = vm::setName,
            placeholder = {
                Text("Name")
            },
            label = {
                Text("Name")
            },
            singleLine = true,
            modifier = Modifier
                .padding(16.dp)
                .focusRequester(nameTf),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = { artistTf.requestFocus() }
            )
        )
        OutlinedTextField(
            value = vm.artist.value,
            onValueChange = vm::setArtist,
            placeholder = {
                Text("Artist")
            },
            singleLine = true,
            label = {
                Text("Artist")
            },
            modifier = Modifier
                .padding(16.dp)
                .focusRequester(artistTf),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            keyboardActions = KeyboardActions(
                onNext = { trackTf.requestFocus() }
            )
        )
        OutlinedTextField(
            value = vm.track.value,
            onValueChange = vm::setTrack,
            placeholder = {
                Text("Track")
            },
            label = {
                Text("Track")
            },
            singleLine = true,
            modifier = Modifier
                .padding(16.dp)
                .focusRequester(trackTf),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Is Awesome",
                modifier = Modifier.padding(end = 5.dp)
            )
            Checkbox(
                checked = vm.isAwesome.value,
                onCheckedChange = vm::setAwesome
            )
        }
        Button(
            onClick = {
                try {
                    val song = vm.validate()
                    onAddSong(song)
                } catch(e: Exception) {
                    Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Add Song")
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LabsAppTheme {
        val vm = NewSongViewModel()
        NewSongView(vm, {})
    }
}
