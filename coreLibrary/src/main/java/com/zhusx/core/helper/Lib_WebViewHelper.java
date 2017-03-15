package com.zhusx.core.helper;

import android.annotation.SuppressLint;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.zhusx.core.debug.LogUtil;

import java.util.Locale;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/4/5 10:01
 */
public class Lib_WebViewHelper {
    private WebView mWebView;
    public String errorMessage = "网络不稳定或已断开";
    public String retryButtonText = "您可以点击刷新按钮，再次加载";

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public Lib_WebViewHelper(WebView mWebView) {
        this.mWebView = mWebView;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "zhusx");
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (LogUtil.DEBUG) {
            LogUtil.e(this, String.format(Locale.CHINA, "%d:%s:%s", errorCode, description, failingUrl));
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<html>");
        sb.append("<head>");
        sb.append("<title>加载失败</title>");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> ");
        sb.append("<script>");
        sb.append("function reLoad(){");
        sb.append("window.zhusx.libReLoad(\"" + failingUrl + "\");");
        sb.append("}");
        sb.append("</script>");
        sb.append("</head>");
        sb.append("<body style=\"position:fixed;top:50%;left:50%;text-align:center;-webkit-transform:translateX(-50%) translateY(-50%);\">");
        //sb.append("<img src=\"file:///android_res/drawable/netbreak.png\" style=\"width:40%\"/>");
        sb.append("<p style=\"margin-bottom:0px\">" + errorMessage + "</p>");
        sb.append("<p style=\"margin-top:10px;color:#a5a5a5;font-size:80%;width:200px;\">" + retryButtonText + "</p>");
        sb.append("<p style=\"margin-top:10px;padding:10px;-webkit-border-radius:5px;background-color:#e42a2d;color:#ffffff;\" onclick=\"reLoad()\">刷新页面</p>");
        sb.append("</body>");
        sb.append("</html>");
        view.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", null);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    boolean isFinish = true;

    @JavascriptInterface
    public void libReLoad(final String url) {
        if (!isFinish) {
            return;
        }
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(url);
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.clearHistory();
                        isFinish = true;
                    }
                }, 500);
            }
        });
    }
}
