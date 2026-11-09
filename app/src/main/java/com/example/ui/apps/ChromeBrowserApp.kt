package com.example.ui.apps

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.theme.ChromeGlassBorder
import com.example.ui.theme.ChromeSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class BrowserTab(
    val id: String,
    var title: String,
    var url: String
)

data class Bookmark(
    val name: String,
    val url: String
)

@Composable
fun ChromeBrowserApp(
    initialUrl: String = "",
    modifier: Modifier = Modifier
) {
    val defaultBookmarkList = listOf(
        Bookmark("Google", "https://www.google.com"),
        Bookmark("YouTube", "https://www.youtube.com"),
        Bookmark("Gemini AI", "https://gemini.google.com"),
        Bookmark("GitHub", "https://github.com"),
        Bookmark("Wikipedia", "https://www.wikipedia.org"),
        Bookmark("Hacker News", "https://news.ycombinator.com")
    )

    val startUrl = if (initialUrl.isNotBlank()) initialUrl else "https://www.google.com"

    val tabs = remember {
        mutableStateListOf(
            BrowserTab(
                id = "tab_1",
                title = "New Tab",
                url = startUrl
            )
        )
    }

    var activeTabId by remember { mutableStateOf("tab_1") }
    val activeTab = tabs.find { it.id == activeTabId } ?: tabs.first()

    var omniboxInput by remember(activeTabId, activeTab.url) { mutableStateOf(activeTab.url) }
    var isDesktopUserAgent by remember { mutableStateOf(true) }
    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    val DESKTOP_UA = "Mozilla/5.0 (X11; CrOS x86_64 14541.0.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF161326))
    ) {
        // Tab Bar Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(36.dp)
                .background(Color(0xFF110E21))
                .padding(horizontal = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LazyRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(tabs, key = { it.id }) { tab ->
                    val isActive = tab.id == activeTabId
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(160.dp)
                            .height(30.dp)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(if (isActive) Color(0xFF1E1B2E) else Color(0xFF161226))
                            .border(
                                width = 1.dp,
                                color = if (isActive) NeonCyan.copy(alpha = 0.5f) else Color.Transparent,
                                shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                            )
                            .clickable {
                                activeTabId = tab.id
                                omniboxInput = tab.url
                            }
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = tab.title,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isActive) TextPrimary else TextSecondary,
                                fontSize = 11.sp,
                                fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        if (tabs.size > 1) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close Tab",
                                tint = TextSecondary,
                                modifier = Modifier
                                    .size(14.dp)
                                    .clickable {
                                        val index = tabs.indexOf(tab)
                                        tabs.remove(tab)
                                        if (isActive && tabs.isNotEmpty()) {
                                            activeTabId = tabs[index.coerceAtMost(tabs.size - 1)].id
                                        }
                                    }
                            )
                        }
                    }
                }
            }

            // New Tab button
            IconButton(
                onClick = {
                    val newId = "tab_${System.currentTimeMillis()}"
                    tabs.add(BrowserTab(id = newId, title = "New Tab", url = "https://www.google.com"))
                    activeTabId = newId
                    omniboxInput = "https://www.google.com"
                },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Tab",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Navigation Bar & Omnibox
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E1B2E))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { webViewInstance?.goBack() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = { webViewInstance?.goForward() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            IconButton(
                onClick = { webViewInstance?.reload() },
                modifier = Modifier.size(30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = TextPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(6.dp))

            // URL Omnibox
            OutlinedTextField(
                value = omniboxInput,
                onValueChange = { omniboxInput = it },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "SSL Secure",
                        tint = NeonCyan,
                        modifier = Modifier.size(14.dp)
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF28233E),
                    unfocusedContainerColor = Color(0xFF201B35),
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = ChromeGlassBorder,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                modifier = Modifier
                    .weight(1f)
                    .height(38.dp)
                    .testTag("browser_omnibox"),
                textStyle = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Desktop User Agent Toggle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    imageVector = if (isDesktopUserAgent) Icons.Default.Computer else Icons.Default.Smartphone,
                    contentDescription = "Desktop UA",
                    tint = if (isDesktopUserAgent) NeonCyan else TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Switch(
                    checked = isDesktopUserAgent,
                    onCheckedChange = {
                        isDesktopUserAgent = it
                        webViewInstance?.settings?.userAgentString = if (it) DESKTOP_UA else null
                        webViewInstance?.reload()
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = NeonCyan,
                        checkedTrackColor = Color(0xFF2B2544)
                    ),
                    modifier = Modifier
                        .height(24.dp)
                        .testTag("desktop_ua_toggle")
                )
            }
        }

        // Bookmarks Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF161226))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            defaultBookmarkList.forEach { bookmark ->
                Text(
                    text = bookmark.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier
                        .clickable {
                            activeTab.url = bookmark.url
                            omniboxInput = bookmark.url
                            webViewInstance?.loadUrl(bookmark.url)
                        }
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }

        // Main WebView Content Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = true
                        settings.userAgentString = DESKTOP_UA

                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                url?.let {
                                    activeTab.url = it
                                    omniboxInput = it
                                }
                                view?.title?.let {
                                    activeTab.title = it
                                }
                            }
                        }
                        webChromeClient = WebChromeClient()
                        loadUrl(activeTab.url)
                        webViewInstance = this
                    }
                },
                update = { webView ->
                    webViewInstance = webView
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
