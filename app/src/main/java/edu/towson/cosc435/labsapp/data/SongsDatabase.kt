package edu.towson.cosc435.labsapp.data

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Update
import edu.towson.cosc435.labsapp.data.model.Song

@Dao
interface SongsDao {
    @Query("select id, name, artist, track, is_awesome, icon_url from songs")
    suspend fun getSongs(): List<Song>

    @Insert
    suspend fun addSong(song: Song)

    @Delete
    suspend fun deleteSong(song: Song)

    @Update
    suspend fun updateSong(song: Song)
}

@Database(entities = [Song::class], version = 2, exportSchema = false)
abstract class SongsDatabase : RoomDatabase() {
    abstract fun songsDao(): SongsDao
}









