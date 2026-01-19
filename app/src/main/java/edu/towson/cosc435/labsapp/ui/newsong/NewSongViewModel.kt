package edu.towson.cosc435.labsapp.ui.newsong

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import edu.towson.cosc435.labsapp.data.model.Song
import java.util.UUID

class NewSongViewModel : ViewModel() {
    private val _name: MutableState<String> = mutableStateOf("")
    val name: State<String> = _name
    private val _artist: MutableState<String> = mutableStateOf("")
    val artist: State<String> = _artist
    private val _track: MutableState<String> = mutableStateOf("")
    val track: State<String> = _track
    private val _isAwesome: MutableState<Boolean> = mutableStateOf(false)
    val isAwesome: State<Boolean> = _isAwesome

    fun setName(name: String) {
        _name.value = name
    }

    fun setArtist(artist: String) {
        _artist.value = artist
    }

    fun setTrack(track: String) {
        _track.value = track
    }

    fun setAwesome(isAwesome: Boolean) {
        _isAwesome.value = isAwesome
    }

    /**
     * Validate the input fields and will throw an exception if not valid.
     * Otherwise, a valid Song object is returned
     */
    fun validate(): Song {
        if(name.value.isEmpty()) {
            throw Exception("Song name needed")
        }
        if(artist.value.isEmpty()) {
            throw Exception("Artist name needed")
        }
        if(track.value.isEmpty()) {
            throw Exception("Track number needed")
        }
        val trackInt = track.value.toIntOrNull() ?: throw Exception("Track must be an integer")
        return Song(UUID.randomUUID().toString(), name.value, artist.value, trackInt, isAwesome.value, "")
    }
}