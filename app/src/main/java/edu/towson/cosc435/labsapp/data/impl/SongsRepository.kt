package edu.towson.cosc435.labsapp.data.impl

import edu.towson.cosc435.labsapp.data.ISongsRepository
import edu.towson.cosc435.labsapp.data.model.Song
import kotlinx.coroutines.delay

class SongsRepository : ISongsRepository {

    private var _songs = listOf<Song>()

    init {
        _songs = (0..20).map { i ->
            Song(i.toString(), "Song $i", "Artist $i", i, i % 3 == 0, "")
        }
    }

    override suspend fun getSongs(): List<Song> {
        return _songs
    }

    override suspend fun deleteSong(song: Song) {
        delay(2000)
        _songs = _songs.filter { s -> s.id != song.id }
    }

    override suspend fun addSong(song: Song) {
        delay(2000)
        _songs = listOf(song) + _songs
    }

    override suspend fun toggleAwesome(song: Song) {
        delay(2000)
        _songs = _songs.map { s ->
            if(s.id == song.id) {
                s.copy(isAwesome = !s.isAwesome)
            } else {
                s
            }
        }
    }
}