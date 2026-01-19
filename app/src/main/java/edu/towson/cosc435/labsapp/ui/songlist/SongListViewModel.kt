package edu.towson.cosc435.labsapp.ui.songlist

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import edu.towson.cosc435.labsapp.data.ISongsRepository
import edu.towson.cosc435.labsapp.data.impl.SongsDatabaseRepository
import edu.towson.cosc435.labsapp.data.impl.SongsRepository
import edu.towson.cosc435.labsapp.data.model.Song
import edu.towson.cosc435.labsapp.network.ISongsFetcher
import edu.towson.cosc435.labsapp.network.SongsFetcher
import edu.towson.cosc435.labsapp.workers.DownloadImageWorker
import kotlinx.coroutines.launch
import java.lang.Exception

class SongListViewModel(app: Application) : AndroidViewModel(app) {
    private val _songs: MutableState<List<Song>> = mutableStateOf(listOf())
    val songs: State<List<Song>> = _songs

    private val _selected: MutableState<Song?>
    val selectedSong: State<Song?>
    private val _waiting: MutableState<Boolean>
    val waiting: State<Boolean>
    val dialog: DeleteDialog = DeleteDialog()

    private val _repository: ISongsRepository = SongsDatabaseRepository(app)
    private val _songsFetcher: ISongsFetcher = SongsFetcher(getApplication())

    // TODO - 1. Ask for location permissions
    // TODO - 2. Subscribe to location changes
    // TODO - 3. Display the location below the search bar
    // TODO - 4. Clicking the location should launch the Maps app

    init {
        _selected = mutableStateOf(null)
        selectedSong = _selected
        _waiting = mutableStateOf(false)
        waiting = _waiting
        viewModelScope.launch {
            _songs.value = _repository.getSongs()
            if(_songs.value.isEmpty()) {
                try {
                    _waiting.value = true
                    val songs = _songsFetcher.fetchSongs()
                    songs.forEach { song -> _repository.addSong(song) }
                    _songs.value = _repository.getSongs()
                } catch (e: Exception) {
                    Log.e(this@SongListViewModel.javaClass.simpleName, e.message, e)
                } finally {
                    _waiting.value = false
                }
            }
        }
    }

    fun addSong(song: Song) {
        viewModelScope.launch {
            _waiting.value = true
            _repository.addSong(song)
            _songs.value = _repository.getSongs()
            _waiting.value = false
        }
    }

    fun deleteSong() {
        if(dialog.songToDelete == null) return
        viewModelScope.launch {
            _waiting.value = true
            _repository.deleteSong(dialog.songToDelete!!)
            _songs.value = _repository.getSongs()
            _waiting.value = false
        }
    }

    fun toggleAwesome(song: Song) {
        viewModelScope.launch {
            _waiting.value = true
            _repository.toggleAwesome(song)
            _songs.value = _repository.getSongs()
            _waiting.value = false
        }
    }

    fun selectSong(song: Song) {
        _selected.value = song
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val workRequest = OneTimeWorkRequestBuilder<DownloadImageWorker>()
            .setConstraints(constraints)
            .setInputData(workDataOf(
                DownloadImageWorker.INPUT_KEY to song.iconUrl,
                DownloadImageWorker.INPUT_NAME_KEY to song.name
            ))
            .build()
        WorkManager.getInstance(getApplication()).enqueue(workRequest)
    }

    private fun subscribeToLocationUpdates() {
//        val req = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10 * 1000).build()
//        val client = LocationServices.getFusedLocationProviderClient(getApplication<Application>())
//        client.requestLocationUpdates(req, object: LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                super.onLocationResult(result)
//                // TODO - Set the location state
//            }
//        }, Looper.getMainLooper())
    }

    fun openMaps(location: String) {
        // TODO - 4c. Launch GoogleMaps app
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("geo:" + location)
        getApplication<Application>().startActivity(intent)
    }

    class DeleteDialog {
        private val _showDialog: MutableState<Boolean> = mutableStateOf(false)
        val showDialog: State<Boolean> = _showDialog

        var songToDelete: Song? = null
            private set

        fun hideDialog() {
            songToDelete = null
            _showDialog.value = false
        }
        fun showDialog(song: Song) {
            songToDelete = song
            _showDialog.value = true
        }
    }
}