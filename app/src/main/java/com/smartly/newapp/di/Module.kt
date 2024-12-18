package com.example.movieapp.di


import android.content.Context
import androidx.room.Room
import com.smartly.newapp.local.MyDao
import com.smartly.newapp.local.MyDatabase
import com.smartly.newapp.network.ApiServices
import com.smartly.newapp.utils.Constants.Companion.BASE_URL

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
@InstallIn(SingletonComponent::class)
@Module
object Module {

    @Singleton
    @Provides
    fun getOkHttp():OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor({chain->
                val original=chain.request()
                val requestBuilder=original.newBuilder()
                    .header("Authorization",
                        "bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5Mjg3MzBkY2FiMTUzY2ViODQ4ZDhmMTJhNjQ3NTk2ZSIsIm5iZiI6MTcyODkxMTM3Mi4wMjIyMDMsInN1YiI6IjY3MGQxNWUyZjU4YTkyMDZhYTQxNjY0OCIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.UJCiQ12PlkXBhJ4UP05GZFZtybuptoHJdMRHaUKdkmI")
                    .build()
                chain.proceed(requestBuilder)
            })
            .build()
    }

    @Singleton
    @Provides
    fun  getRetrofit(okHttp:OkHttpClient):Retrofit{
        return Retrofit.Builder()
            .client(okHttp)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun getApiServices(retrofit: Retrofit) : ApiServices {
      return  retrofit.create(ApiServices::class.java)
    }

    @Singleton
    @Provides
    fun getMyDatabase(@ApplicationContext context: Context) : MyDatabase {
        return Room.databaseBuilder(context.applicationContext, MyDatabase::class.java
            ,"article_db.db")
//                .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
    @Singleton
    @Provides
    fun getMyDao(myDatabase: MyDatabase): MyDao {
        return myDatabase.getDao()
    }
}