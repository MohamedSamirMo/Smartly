package com.smartly.newapp.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smartly.newapp.models.Article


@Dao
interface MyDao {
    // Room Database with Coroutines
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article) :Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>
    @Delete
    suspend fun deleteAllArticles(article: Article)

}