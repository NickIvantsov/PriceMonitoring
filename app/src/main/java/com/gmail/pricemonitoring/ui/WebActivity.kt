package com.gmail.pricemonitoring.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.gmail.pricemonitoring.R


class WebActivity : AppCompatActivity() {

    companion object {
        const val URL_VALUE = "url_value"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        val url: String = intent.getStringExtra(URL_VALUE) ?: "https://www.citrus.ua/"

        val browser = findViewById<View>(R.id.webBrowser) as WebView
        browser.getSettings().setJavaScriptEnabled(true)
        browser.loadUrl(Uri.parse(url).toString())
    }
}
