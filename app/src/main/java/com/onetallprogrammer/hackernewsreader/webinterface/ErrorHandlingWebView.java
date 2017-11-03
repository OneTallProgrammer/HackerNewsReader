package com.onetallprogrammer.hackernewsreader.webinterface;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by joseph on 9/4/17.
 */

public class ErrorHandlingWebView extends WebViewClient {


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
    }
}
