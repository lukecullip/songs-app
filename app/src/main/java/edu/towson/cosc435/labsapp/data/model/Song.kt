package edu.towson.cosc435.labsapp.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "songs") // this creates a table
data class Song(
    @PrimaryKey
    val id: String,
    val name: String,
    val artist: String,
    @SerializedName("track_num")
    val track: Int,
    @ColumnInfo(name = "is_awesome") // database column name
    @SerializedName("is_awesome")
    val isAwesome: Boolean,
    @SerializedName("icon_url")
    @ColumnInfo("icon_url") // database column name
    val iconUrl: String
) {
}