package edu.towson.cosc435.labsapp.network

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.Gson
import edu.towson.cosc435.labsapp.data.model.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.io.OutputStream

interface ISongsFetcher {
    suspend fun fetchSongs(): List<Song>
    suspend fun fetchIcon(url: String): Bitmap?
    suspend fun fetchAndSave(url: String, file: OutputStream)
}

class SongsFetcher(private val ctx: Context) : ISongsFetcher {
    private val URL = "https://my-json-server.typicode.com/rvalis-towson/lab_api/songs"
    private val client = OkHttpClient.Builder()
        .cache(
            Cache(
                directory = ctx.cacheDir,
                maxSize = 10 * 1024L * 1024L
            )
        )
        .build()
    override suspend fun fetchSongs(): List<Song> {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .get()
                .url(URL)
                .build()
            val response = client.newCall(request).execute()
            val responseBody = response.body
            if(responseBody != null) {
                val jsonString = responseBody.string()
                val gson = Gson()
                val songsArray = gson.fromJson(jsonString, Array<Song>::class.java)
                songsArray.toList()
            } else {
                listOf()
            }
        }
    }

    override suspend fun fetchIcon(url: String): Bitmap? {
        val stream = fetchBytes(url)
        return BitmapFactory.decodeStream(stream)
    }

    override suspend fun fetchAndSave(url: String, file: OutputStream) {
        val stream = fetchBytes(url)
        file.bufferedWriter()
        val bytes = stream?.readBytes()
        if(bytes != null) {
            withContext(Dispatchers.IO) {
                file.write(bytes)
            }
        }
    }

    private suspend fun fetchBytes(url: String): InputStream? {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .get()
                .url(url)
                .build()
            val response = client.newCall(request).execute()
            response.body?.byteStream()
        }
    }
}