package com.smartly.newapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.smartly.newapp.R
import com.smartly.newapp.databinding.FragmentArticaleBinding
import com.smartly.newapp.ui.MainActivity
import com.smartly.newapp.viewmodel.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ArticaleFragment : Fragment(R.layout.fragment_articale) {

    lateinit var viewModel: NewsViewModel
    private var _binding: FragmentArticaleBinding? = null
    private val binding get() = _binding!!
    val args: ArticaleFragmentArgs by navArgs()

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
        _binding = FragmentArticaleBinding.bind(view)
        viewModel = (activity as MainActivity).viewModel

        val article = args.article

        // التحقق من URL قبل تحميله
        if (article.url.isNullOrEmpty()) {
            Snackbar.make(view, "Invalid URL", Snackbar.LENGTH_SHORT).show()
        } else {
            binding.webView.apply {
                settings.javaScriptEnabled = true  // تمكين JavaScript
                webViewClient = WebViewClient()  // لتجنب فتح المتصفح الخارجي
                loadUrl(article.url)
            }
        }

        // إضافة المقال إلى المفضلة
        binding.fab.setOnClickListener {
            viewModel.addToFavourites(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null  // التأكد من تنظيف الـ binding بشكل صحيح
    }
}
