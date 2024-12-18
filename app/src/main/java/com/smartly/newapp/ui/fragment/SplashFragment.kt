package com.smartly.newapp.ui.fragment

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.smartly.newapp.R
import com.smartly.newapp.databinding.FragmentSplashBinding

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.hide()
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

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

        _binding = FragmentSplashBinding.bind(view)


        startApp()

    }

    private fun startApp() {
        Handler(Looper.getMainLooper()).postDelayed({

            findNavController().navigate(R.id.action_splashFragment_to_headlinesFragment)
        }, 4000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}