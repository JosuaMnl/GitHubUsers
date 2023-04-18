package com.yosha10.githubusers.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FavoriteUser (
    @PrimaryKey(autoGenerate = false)
    var username: String = "",
    @ColumnInfo(name = "avatar_url")
    var avatarUrl: String? = null
): Parcelable