package edu.towson.cosc435.labsapp.data.impl

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.towson.cosc435.labsapp.data.ISongsRepository
import edu.towson.cosc435.labsapp.data.SongsDatabase
import edu.towson.cosc435.labsapp.data.model.Song

class SongsDatabaseRepository(app: Application) : ISongsRepository {

    private val songsDatabase: SongsDatabase

    init {
        songsDatabase = Room.databaseBuilder(
            app,
            SongsDatabase::class.java,
            "songs.db"
        ).fallbackToDestructiveMigration() // deletes the db on version change
            .build()
    }

    override suspend fun getSongs(): List<Song> {
        return songsDatabase.songsDao().getSongs()
    }

    override suspend fun deleteSong(song: Song) {
        songsDatabase.songsDao().deleteSong(song)
    }

    override suspend fun addSong(song: Song) {
        songsDatabase.songsDao().addSong(song)
    }

    override suspend fun toggleAwesome(song: Song) {
        val newSong = song.copy(isAwesome = !song.isAwesome)
        songsDatabase.songsDao().updateSong(newSong)
    }
}