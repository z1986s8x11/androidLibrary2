package com.zhusx.core.network;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._HttpURLRequests;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import static com.zhusx.core.utils._HttpURLRequests.encodeUrl;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/8/22 16:29
 */

public class Request {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String UPLOAD = "UPLOAD";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    public String url;
    public String method;
    public String contentType;
    public String body;
    public Map<String, String> header;
    public int connectionTimeOut;
    public int readTimeOut;
    public int downloadCacheTime;
    public _HttpURLRequests.OnProgressListener listener;

    private Request() {
    }

    private Request(String url, String method, String contentType, Map<String, String> header, String body) {
        this.url = url;
        this.method = method;
        this.contentType = contentType;
        this.body = body;
        this.header = header;
    }

    public static Builder createRequest() {
        return new Builder();
    }

    public static class Builder {
        String url;
        String method = "GET";
        String contentType;
        String body;
        Map<String, String> bodyMap;
        Map<String, String> header;
        int connectionTimeOut;
        int readTimeOut;
        int downloadCacheTime = 10 * 60 * 1000;
        _HttpURLRequests.OnProgressListener listener;

        public Builder setDownloadCacheTime(int downloadCacheTime) {
            if (downloadCacheTime < 0) {
                return this;
            }
            this.downloadCacheTime = downloadCacheTime;
            return this;
        }

        public Builder setProgressListener(_HttpURLRequests.OnProgressListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setUrl(@NonNull String url) {
            this.url = url;
            return this;
        }

        public Builder setMethod(@NonNull String method) {
            this.method = method.toUpperCase();
            return this;
        }

        public Builder setContentType(String contentType) {
            this.contentType = contentType;
            return this;
        }


        public Builder setBody(String body) {
            this.body = body;
            return this;
        }

        public Builder setConnectionTimeOut(int connectionTimeOut) {
            this.connectionTimeOut = connectionTimeOut;
            return this;
        }

        public Builder setReadTimeOut(int readTimeOut) {
            this.readTimeOut = readTimeOut;
            return this;
        }

        public Builder setBody(Map<String, String> body) {
            this.bodyMap = body;
            return this;
        }

        public Builder setHeader(Map<String, String> header) {
            this.header = header;
            return this;
        }

        public Request build() {
            if (TextUtils.isEmpty(url)) {
                throw new IllegalArgumentException("url == null");
            }
            if (connectionTimeOut <= 0) {
                if (Request.DOWNLOAD.equals(method)) {
                    connectionTimeOut = 10000 * 6;
                } else {
                    connectionTimeOut = 10000;
                }
            }
            if (readTimeOut < 0) {
                readTimeOut = 10000;
            }
            switch (method) {
                case Request.GET:
                    if (bodyMap != null && !bodyMap.isEmpty()) {
                        url = encodeUrl(url, bodyMap);
                    }
                    if (!TextUtils.isEmpty(body)) {
                        if (LogUtil.DEBUG) {
                            LogUtil.e("GET is not body txt");
                        }
                        body = "";
                    }
                    break;
                case Request.POST:
                case Request.PUT:
                    StringBuffer sb = new StringBuffer();
                    if (TextUtils.isEmpty(contentType) || "application/x-www-form-urlencoded".equals(contentType)) {
                        if (bodyMap != null && !bodyMap.isEmpty()) {
                            Iterator iterator = bodyMap.keySet().iterator();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();
                                sb.append("&");
                                sb.append(key);
                                sb.append("=");
                                try {
                                    sb.append(URLEncoder.encode(bodyMap.get(key) == null ? "" : String.valueOf(bodyMap.get(key)), "utf-8"));
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                            sb.deleteCharAt(0);
                        }
                        if (!TextUtils.isEmpty(body)) {
                            if (LogUtil.DEBUG) {
                                LogUtil.e("POST or PUT at text/plain is not body txt");
                            }
                        }
                        body = sb.toString();
                    } else {
                        //text/plain
                        //application/json
                        if (bodyMap != null && !bodyMap.isEmpty()) {
                            url = _HttpURLRequests.encodeUrl(url, bodyMap);
                        }
                    }
                    break;
                case Request.DELETE:
                    if (bodyMap != null && bodyMap.size() > 0) {
                        url = encodeUrl(url, bodyMap);
                    }
                    if (!TextUtils.isEmpty(body)) {
                        if (LogUtil.DEBUG) {
                            LogUtil.e("DELETE is not body txt");
                        }
                        body = "";
                    }
                    break;
                case Request.DOWNLOAD:
                    if (TextUtils.isEmpty(body)) {
                        throw new IllegalArgumentException("request.body(FileName) is empty");
                    }
                    break;
                case Request.UPLOAD:
                    if (TextUtils.isEmpty(body)) {
                        throw new IllegalArgumentException("request.body(FileName) is empty");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("method must GET ,POST ,PUT or DELETE,current is : " + method);
            }
            return new Request(url, method, contentType, header, body);
        }
    }
}
