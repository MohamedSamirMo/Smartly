package com.smartly.newapp.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.smartly.newapp.R
import com.smartly.newapp.adapter.NewsAdapter
import com.smartly.newapp.databinding.FragmentHeadlinesBinding
import com.smartly.newapp.models.NewsModel
import com.smartly.newapp.ui.MainActivity
import com.smartly.newapp.utils.Constants.Companion.QUERY_PAGE_SIZE
import com.smartly.newapp.utils.Resourse
import com.smartly.newapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines) {

    lateinit var newsModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var retryButton: Button
    lateinit var errorText: TextView
    lateinit var itemHeadlines: CardView

    private var _binding: FragmentHeadlinesBinding? = null
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
        _binding = FragmentHeadlinesBinding.bind(view)

        itemHeadlines = view.findViewById(R.id.itemHeadlinesError)
        errorText = view.findViewById(R.id.errorText)
        retryButton = view.findViewById(R.id.retryButton)

        newsModel = (activity as MainActivity).viewModel
        setupHeadlineRecyclerView()

        // Set OnClickListener for retry button
        retryButton.setOnClickListener {
            newsModel.getTopHeadlines("us")
        }

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable("article", article)
            }
            findNavController().navigate(R.id.action_headlinesFragment_to_articaleFragment, bundle)
        }

        newsModel.headlines.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resourse.Success<*> -> {
                    hideProgressBar()
                    hideError()
                    response.data?.let { newResponse ->
                        newsAdapter.differ.submitList(newResponse.articles.toList())
                        val totalPages = newResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = newsModel.headlinesPage == totalPages
                        if (isLastPage) {
                            binding.recyclerHeadlines.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resourse.Error<*> -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG).show()
                        showErrorMessage(message)
                        hideRetryButton()
                    }
                }
                is Resourse.Loading<*> -> {
                    showProgressBar()
                }
            }
        })
    }

    var isError = false
    var isLoading = false
    var isScrolling = false
    var isLastPage = false

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideError() {
        itemHeadlines.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        itemHeadlines.visibility = View.VISIBLE
        errorText.text = message
        isError = true
    }

    private fun hideRetryButton() {
        retryButton.visibility = View.INVISIBLE
    }

    private fun showRetryButton() {
        retryButton.visibility = View.VISIBLE
    }

    val scrollerListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNoError = !isError
            val isNoLastPageAndIsNoLoading = !isLastPage && !isLoading

            val isAtLastItems = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNoError && isNoLastPageAndIsNoLoading && isAtLastItems &&
                    isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                newsModel.getTopHeadlines("us")
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

    private fun setupHeadlineRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerHeadlines.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(scrollerListener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
