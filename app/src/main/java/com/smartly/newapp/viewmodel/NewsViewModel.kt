package com.smartly.newapp.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartly.newapp.models.Article
import com.smartly.newapp.models.NewsModel
import com.smartly.newapp.repository.NewsRespository
import com.smartly.newapp.utils.Resourse
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRespository: NewsRespository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    val headlines: MutableLiveData<Resourse<NewsModel>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse: NewsModel? = null

    val searchNews: MutableLiveData<Resourse<NewsModel>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsModel? = null

    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    init {
        getTopHeadlines("us")
    }

    fun getTopHeadlines(countryCode: String) = viewModelScope.launch {
        fetchTopHeadlines(countryCode)
    }

    fun searchForNews(searchQuery: String) = viewModelScope.launch {
        fetchSearchNews(searchQuery)
    }

    private fun handleTopHeadlinesResponse(response: Response<NewsModel>): Resourse<NewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                headlinesPage++
                if (headlinesResponse == null) {
                    headlinesResponse = resultResponse
                } else {
                    headlinesResponse?.articles?.addAll(resultResponse.articles)
                }
                return Resourse.Success(headlinesResponse ?: resultResponse)
            }
        }
        return Resourse.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<NewsModel>): Resourse<NewsModel> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (searchNewsResponse == null || newSearchQuery != oldSearchQuery) {
                    searchNewsPage = 1
                    oldSearchQuery = newSearchQuery
                    searchNewsResponse = resultResponse
                } else {
                    searchNewsPage++
                    searchNewsResponse?.articles?.addAll(resultResponse.articles)
                }
                return Resourse.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resourse.Error(response.message())
    }

    fun addToFavourites(article: Article) = viewModelScope.launch {
        newsRespository.insert(article)
    }

    fun getFavourites() = newsRespository.getAllArticlesFavourites()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRespository.deleteAllArticles(article)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.run {
            hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
        }
    }

    private suspend fun fetchTopHeadlines(countryCode: String) {
        headlines.postValue(Resourse.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRespository.getTopHeadlines(countryCode, headlinesPage)
                headlines.postValue(handleTopHeadlinesResponse(response))
            } else {
                headlines.postValue(Resourse.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headlines.postValue(Resourse.Error("Network Failure"))
                else -> headlines.postValue(Resourse.Error("Conversion Error"))
            }
        }
    }

    private suspend fun fetchSearchNews(searchQuery: String) {
        newSearchQuery = searchQuery
        searchNews.postValue(Resourse.Loading())
        try {
            if (hasInternetConnection()) {
                val response = newsRespository.searchForNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else {
                searchNews.postValue(Resourse.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resourse.Error("Network Failure"))
                else -> searchNews.postValue(Resourse.Error("Conversion Error"))
            }
        }
    }
}
