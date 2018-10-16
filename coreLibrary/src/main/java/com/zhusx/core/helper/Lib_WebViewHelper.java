package com.zhusx.core.helper;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;
import android.widget.ProgressBar;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._Densitys;

import java.util.Locale;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2016/4/5 10:01
 */
public class Lib_WebViewHelper {
    private WebView mWebView;
    public String errorMessage = "网络不稳定或已断开";
    public String errorMessageSub = "您可以点击刷新按钮，再次加载";
    public String retryButton = "刷新页面";
    public String errorMessageColor = "#a5a5a5";
    public String buttonBackgroundColor = "#e42a2d";
    public String buttonTextColor = "#ffffff";
    private ProgressBar progressbar;
    private boolean isFinish = true;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    public Lib_WebViewHelper(WebView mWebView) {
        this.mWebView = mWebView;
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "zhusx");
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (LogUtil.DEBUG) {
            LogUtil.e(String.format(Locale.CHINA, "%d:%s:%s", errorCode, description, failingUrl));
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
        sb.append("<p style=\"margin-top:10px;color:" + errorMessageColor + ";font-size:80%;width:200px;\">" + errorMessageSub + "</p>");
        sb.append("<p style=\"margin-top:10px;padding:10px;-webkit-border-radius:5px;background-color:" + buttonBackgroundColor + ";color:" + buttonTextColor + ";\" onclick=\"reLoad()\">" + retryButton + "</p>");
        sb.append("</body>");
        sb.append("</html>");
        view.loadDataWithBaseURL(null, sb.toString(), "text/html", "UTF-8", null);
    }

    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (url.startsWith("tel:")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
        } else if (url.startsWith("weixin://wap/pay?")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else if (url.startsWith("alipays://platformapi/startApp")) {
            try {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                view.getContext().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            view.loadUrl(url);
        }
        return true;
    }

    public void onProgressChanged(WebView view, int newProgress, @DrawableRes int resId) {
        if (progressbar == null) {
            progressbar = new ProgressBar(view.getContext(), null, android.R.attr.progressBarStyleHorizontal);
            progressbar.setLayoutParams(new AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, _Densitys.dip2px(view.getContext(), 2), 0, 0));
            progressbar.setMax(100);
            if (resId != 0 && resId != -1) {
                progressbar.setProgressDrawable(view.getResources().getDrawable(resId));
            }
            view.addView(progressbar);
        }
        if (newProgress >= 100) {
            progressbar.setVisibility(View.GONE);
        } else {
            if (progressbar.getVisibility() == View.GONE) {
                progressbar.setVisibility(View.VISIBLE);
            }
            progressbar.setProgress(newProgress);
        }
    }

    public void onProgressChanged(WebView view, int newProgress) {
        this.onProgressChanged(view, newProgress, -1);
    }

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

    public void addImgClick(WebView webView) {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\"); " +
                "for(var i=0;i<objs.length;i++){  " +
                "    objs[i].onclick=function(){  " +
                "        window.ajapi.openImage(this.src);  " +
                "    }  " +
                "}" +
                "})()");
    }
}
