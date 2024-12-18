package com.smartly.newapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class NewsModel(
    val articles: MutableList<Article>,
    val status: String,
    val totalResults: Int
)

@Entity("articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?= null,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
): Serializable

data class Source(
    val id: String?,
    val name: String?
)