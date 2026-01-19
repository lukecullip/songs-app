package edu.towson.cosc435.labsapp.data

import edu.towson.cosc435.labsapp.data.model.Song

interface ISongsRepository {
    suspend fun getSongs(): List<Song>
    suspend fun deleteSong(song: Song)
    suspend fun addSong(song: Song)
    suspend fun toggleAwesome(song: Song)
}