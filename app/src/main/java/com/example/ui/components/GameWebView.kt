package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.data.UserEquipment

class WebAppInterface(
    private val onRunCompleted: (Int, Int, Int, Float, Boolean) -> Unit
) {
    @JavascriptInterface
    fun onGameOver(yards: Int, xp: Int, coins: Int, maxMultiplier: Float, isFlawless: Boolean) {
        onRunCompleted(yards, xp, coins, maxMultiplier, isFlawless)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun GameWebView(
    equipment: UserEquipment,
    onRunCompleted: (Int, Int, Int, Float, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
            addJavascriptInterface(
                WebAppInterface(onRunCompleted),
                "AndroidBridge"
            )
            loadUrl("file:///android_asset/game.html")
        }
    }

    // Sync equipment loadout to JS engine whenever equipment changes
    LaunchedEffect(equipment) {
        val js = "window.setEquipmentLoadout(${equipment.helmetLevel}, ${equipment.padsLevel}, ${equipment.cleatsLevel}, ${equipment.glovesLevel}, ${equipment.ballLevel});"
        webView.evaluateJavascript(js, null)
    }

    AndroidView(
        factory = { webView },
        modifier = modifier.fillMaxSize()
    )
}
