package com.ppyy.android.mc.ui;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ppyy.android.mc.R;
import com.wangtao.universallylibs.BaseActivity;

public class TestActivity extends BaseActivity {
    private String url="http://192.168.1.251:8080/treatwo/webpage/lottery.html?userId=72616";
    private WebView webview;
    @Override
    public void initShowLayout() {
        setContentView(R.layout.activity_test);
        webview= (WebView) findViewById(R.id.test_webview);
    }

    @SuppressLint("JavascriptInterface")
    @Override
    public void setAllData() {
        webview.setWebChromeClient(new WebChromeClient());
        webview.loadUrl(url);
        WebSettings setting = webview.getSettings();
        setting.setJavaScriptEnabled(true);
//        setting.setSupportZoom(false);
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
        setting.setBuiltInZoomControls(false);

        webview.addJavascriptInterface(new TestWeb(),"jsInterface");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:giveRedEnvelopes()");
                doShowToast("调用js");
            }
        });
    }
    private class TestWeb{
        @JavascriptInterface
        public void onStartTicketPage(int index) {
            doShowMesage("js主动调用的+"+index);
        }
    }

}
