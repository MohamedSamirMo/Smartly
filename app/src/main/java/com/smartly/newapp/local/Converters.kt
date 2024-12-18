package com.smartly.newapp.local

import androidx.room.TypeConverter
import com.smartly.newapp.models.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name

    }
    @TypeConverter
    fun toSource(name: String): Source {
     return Source(name,name)
    }
}