package com.smartly.newapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.smartly.newapp.models.Article


@Database(entities = [Article::class], version = 3  , exportSchema = false)
@TypeConverters(Converters::class)
abstract class MyDatabase: RoomDatabase() {
    abstract fun getDao(): MyDao

}

