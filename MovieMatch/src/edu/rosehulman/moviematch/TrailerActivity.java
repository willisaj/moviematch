package edu.rosehulman.moviematch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
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
		
		mWebView.setPadding(0, 0, 0, 0);
		mWebView.setInitialScale(getScale());

		mWebView.loadUrl(mTrailerUrl);
		mWebView.setWebChromeClient(new WebChromeClient() {
		});

		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}

			public void onPageFinished(WebView view, String url) {
				mWebView.loadUrl("javascript:(function(){"
						+ "l=document.getElementById('playbutton');"
						+ "e=document.createEvent('HTMLEvents');"
						+ "e.initEvent('click',true,true);"
						+ "l.dispatchEvent(e);" + "})()");
			}
		});
	}

	@Override
	protected void onDestroy() {
		mWebView.loadUrl("");
		super.onDestroy();
	}
	
	private int getScale(){
	    Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); 
	    int width = display.getWidth(); 
	    Double val = new Double(width)/new Double(640);
	    val = val * 100d;
	    return val.intValue();
	}
}
