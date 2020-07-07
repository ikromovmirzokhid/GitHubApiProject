package com.imb.githubapiproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.imb.githubapiproject.R
import kotlinx.android.synthetic.main.web_view_fragment.*

class GitReposWebViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.web_view_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val URL = arguments?.getString("URL", "")
        webView.webViewClient = WebViewClient()
        webView.loadUrl(URL)

        webView.settings.javaScriptEnabled = true
    }
}