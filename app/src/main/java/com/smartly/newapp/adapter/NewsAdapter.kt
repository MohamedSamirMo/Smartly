package com.smartly.newapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smartly.newapp.R
import com.smartly.newapp.models.Article

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    // step 1: create list of data
    inner class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    lateinit var articleImage: ImageView
    lateinit var articleTitle: TextView
    lateinit var articleSource:TextView
    lateinit var articleDataTime:TextView
    lateinit var articleDescription:TextView

    // using DiffUtil  step 3
    private val DifferCallback=object :DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }

    }
    val differ= AsyncListDiffer(this,DifferCallback)


    // step 2: create adapter
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ArticleViewHolder {
        return ArticleViewHolder(LayoutInflater.from(p0.context)
            .inflate(R.layout.item_news,p0,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    //step 5: running
    override fun onBindViewHolder(holder: ArticleViewHolder, postion: Int) {
        val article=differ.currentList[postion]
        holder.itemView.apply {
            articleImage=findViewById(R.id.articleImage)
            articleTitle=findViewById(R.id.articleTitle)
            articleSource=findViewById(R.id.articleSource)
            articleDataTime=findViewById(R.id.articleDateTime)
            articleDescription=findViewById(R.id.articleDescription)}

            holder.itemView.apply {
                Glide.with(this).load(article.urlToImage).into(articleImage)
                articleTitle.text=article.title
                articleSource.text=article.source?.name
                articleDescription.text=article.description
                articleDataTime.text=article.publishedAt
                setOnClickListener {
                    onItemClickListener?.let { it(article) }

            }
        }

    }

    // step 4: click listener
    private var onItemClickListener:((Article) -> Unit)?=null

    // step 6: click listener
    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }

}