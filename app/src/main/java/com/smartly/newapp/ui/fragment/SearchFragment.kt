package com.smartly.newapp.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartly.newapp.R
import com.smartly.newapp.adapter.NewsAdapter
import com.smartly.newapp.databinding.FragmentFavouritesBinding
import com.smartly.newapp.databinding.FragmentSearchBinding
import com.smartly.newapp.ui.MainActivity
import com.smartly.newapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.smartly.newapp.utils.Constants.Companion.SEARCH_NEWS_TIME_DELAY
import com.smartly.newapp.utils.Resourse
import com.smartly.newapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var rateryButton : Button
    lateinit var errorText : TextView
    lateinit var itemSearch: CardView

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // إخفاء شريط التنقل وشريط الحالة
            activity?.window?.decorView?.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION          // يخفي شريط التنقل (Navigation Bar)
                            or View.SYSTEM_UI_FLAG_FULLSCREEN            // يخفي شريط الحالة (Status Bar)
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY      // يجعل واجهة النظام تختفي تلقائيًا مع إمكانية العودة بإيماءة
                    )

            // إخفاء Action Bar (إذا كان موجودًا)
            activity?.actionBar?.hide()
        }
        _binding = FragmentSearchBinding.bind(view)
        itemSearch=view.findViewById(R.id.itemSearchError)
        errorText=view.findViewById(R.id.errorText)
        rateryButton=view.findViewById(R.id.retryButton)

        newsViewModel=(activity as MainActivity).viewModel
        setupSearchRecyclerView()

        val inflater=requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view:View=inflater.inflate(R.layout.item_error,null)

        newsAdapter.setOnItemClickListener {

            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(R.id.action_searchFragment2_to_articaleFragment,bundle)
        }
        var job:Job?=null
        binding.searchEdit.addTextChangedListener() { editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        newsViewModel.searchForNews(editable.toString())
                    }

                }
            }
        }
        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer{ response->
            when(response){
                is Resourse.Success<*> ->{
                    hindProgressBar()
                    hideError()
                    response.data?.let {newResponse->
                        newsAdapter.differ.submitList(newResponse.articles.toList())
                        val totalPages=newResponse.totalResults/ QUERY_PAGE_SIZE+2
                        isLastPage=newsViewModel.searchNewsPage==totalPages
                        if (isLastPage){
                            binding.recyclerSearch.setPadding(0,0,0,0)
                        }
                    }
                }
                is Resourse.Error<*> ->{
                    hindProgressBar()
                    response.message?.let {message->
                        Toast.makeText(activity,"An error occured: $message", Toast.LENGTH_LONG).show()
                        showErrorMassage(message)

                    }
                }
                is Resourse.Loading<*> ->{
                    showProgressBar()

                }

            }
        })
        rateryButton.setOnClickListener({
            if (binding.searchEdit.text.toString().isNotEmpty()) {
                newsViewModel.searchForNews(binding.searchEdit.text.toString())
            }else{
                hideError()
            }
        })
    }
    var isError=false
    var isLoading=false
    var isScrolling=false
    var isLastPage=false

    private fun hindProgressBar(){
        binding.paginationProgressBar.visibility=View.INVISIBLE
        isLoading=false
    }
    private fun showProgressBar(){
        binding.paginationProgressBar.visibility=View.VISIBLE
        isLoading=true
    }
    private fun hideError(){
        itemSearch.visibility=View.INVISIBLE
        isError=false
    }
    private fun showErrorMassage(message:String) {
        itemSearch.visibility = View.VISIBLE
        errorText.text=message
        isError = true
}

    val ScrollerListener= object :  RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layputManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layputManager.findFirstVisibleItemPosition()
            val visibleItemCount = layputManager.childCount
            val totalItemCount = layputManager.itemCount

            val isNoError = !isError
            val isNoLastPageAndIsNOLoading = !isLastPage && !isLoading

            val isAtlastItems = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoError
                    && isNoLastPageAndIsNOLoading
                    && isAtlastItems && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                newsViewModel.searchForNews(binding.searchEdit.text.toString())
                isScrolling = false
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }

    }
    private fun setupSearchRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerSearch.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchFragment.ScrollerListener)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}