package edu.rosehulman.moviematch;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class TrailerActivity extends Activity {
	
	public static final String KEY_TRAILER_URL = "KEY_TRAILER_URL";
	
	private String mTrailerUrl;
	
	private WebView mWebView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trailer);
		
		mTrailerUrl = getIntent().getStringExtra(KEY_TRAILER_URL);
		
		mWebView = (WebView) findViewById(R.id.webview);
		mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setPluginState(PluginState.ON);
        mWebView.getSettings().setAllowFileAccess(true);
       
        mWebView.loadUrl(mTrailerUrl);
        mWebView.setWebChromeClient(new WebChromeClient() {
        });
             
        mWebView.setWebViewClient(new WebViewClient() {
                 public boolean shouldOverrideUrlLoading(WebView view, String url) {
                     return false;
                }
                public void onPageFinished(WebView view, String url) {
                  //do nothing
                } 
          });
	}
	
	@Override
	protected void onDestroy() {
		mWebView.loadUrl("");
		super.onDestroy();
	}
}
