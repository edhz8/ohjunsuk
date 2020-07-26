package com.example.mywebbrower

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView.apply{
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    urlEditText.setText(url)
                }
            }
        }
        webView.loadUrl(getString(R.string.google_url))

        urlEditText.setOnEditorActionListener{_,actionId,_ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                webView.loadUrl(urlEditText.text.toString())
                true
            } else {
                false
            }
        }
        registerForContextMenu(webView)
    }

    override fun onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack()
        }else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_google,R.id.action_home -> {
                webView.loadUrl(getString(R.string.google_url))
                return true
            }
            R.id.action_naver -> {
                webView.loadUrl(getString(R.string.naver_url))
            }
            R.id.action_fmk -> {
                webView.loadUrl(getString(R.string.fmkorea_url))
            }
            R.id.action_call -> {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:010-4056-6191")
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
                return true
            }
            R.id.action_send_text -> {
                webView.url?.let { sendSMS("010-4056-6191", it) }
                return true
            }
            R.id.action_email -> {
                webView.url?.let { email("edhz8888@gmail.com","테스트 이메일", it) }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context,menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_share -> {
                webView.url?.let { share(it) }
                return true
            }
            R.id.action_browser -> {
                webView.url?.let { browse(it) }
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

}