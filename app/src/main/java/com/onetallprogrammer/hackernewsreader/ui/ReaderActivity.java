package com.onetallprogrammer.hackernewsreader.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.onetallprogrammer.hackernewsreader.R;

public class ReaderActivity extends AppCompatActivity {

    private final String GOOGLE_DOCS_URL = "https://docs.google.com/viewer?url=%s"; // must format

    private String url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        url = getIntent().getStringExtra("url");

        webView = (WebView) findViewById(R.id.readerWebView);

        loadUrl();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.web_view_menu, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){

            case R.id.refreshMenuItem:
                loadUrl();
                break;
            case android.R.id.home:
                if(webView.canGoBack()){
                    webView.goBack();
                } else {
                    this.finish();
                    return true;
                }
            default:
                break;

        }

        return super.onOptionsItemSelected(item);

    }

    public void loadUrl() {

        webView.getSettings().setJavaScriptEnabled(true);

        // check for a bad url
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                if (!Patterns.WEB_URL.matcher(url).matches()) {

                    webView.loadData(
                            String.format(
                                    "<p>Failed to load %s </p>",
                                    url
                            ), null, "UTF-8");

                    Toast.makeText(ReaderActivity.this, "Site Failed To Load!", Toast.LENGTH_SHORT).show();

                }

                super.onPageFinished(view, url);
            }
        });

        if(extIsPdf(url)){

            webView.loadUrl(
                    String.format(GOOGLE_DOCS_URL,url)
            );

        } else {

            webView.loadUrl(url);

        }
    }

    private boolean extIsPdf(String url){

        if (url.length() >= 3) {

            String ext = url.substring(url.length() - 3, url.length());

            return ext.equals("pdf");

        }

        return false;

    }

}
