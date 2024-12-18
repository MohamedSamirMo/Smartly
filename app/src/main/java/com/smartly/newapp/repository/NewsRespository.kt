package com.smartly.newapp.repository

import com.smartly.newapp.local.MyDao
import com.smartly.newapp.models.Article
import com.smartly.newapp.network.ApiServices
import jakarta.inject.Inject

class NewsRespository @Inject constructor(private val apiServices: ApiServices ,private val dao: MyDao) {

    // connect Api
    suspend fun getTopHeadlines(countryCode: String, pageNumber: Int) =
        apiServices.getTopHeadlines(countryCode, pageNumber)

    suspend fun searchForNews(searchQuery: String, pageNumber: Int) =
        apiServices.searchForNews(searchQuery, pageNumber)

    // Saved Database
    suspend fun insert(article: Article) = dao.insert(article)
    fun getAllArticlesFavourites() = dao.getAllArticles()
    suspend fun deleteAllArticles(article: Article) = dao.deleteAllArticles(article)

}