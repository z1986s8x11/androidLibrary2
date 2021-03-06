package com.zhusx.core.network;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求 参数 JavaBean
 * <p>
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 10:02
 */
public class HttpParams {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String UPLOAD = "UPLOAD";
    public static final String DOWNLOAD = "DOWNLOAD";
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_URLENCODED = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";

    private String apiUrl;
    private String method = POST;
    private Object param;
    private boolean isReadHttpCodeErrorMessage = false; //是否读取httpCode > 300的错误信息
    private Map<String, Object> httpHead;  //http头
    boolean isCancel = false;
//    private String contentType;

    public HttpParams() {
    }

    public HttpParams(String url, String method, JSONObject param) {
        this.apiUrl = url;
        this.method = method;
        this.param = param;
    }

    public HttpParams(String url, String mothod,
                      Map<String, Object> param) {
        this.apiUrl = url;
        this.method = mothod;
        this.param = param;
    }

//    public String getContentType() {
//        return contentType;
//    }

//    public void setContentType(String contentType) {
//        this.contentType = contentType;
//    }

    public Object getParam() {
        return param;
    }

    /**
     * @return 请求URL
     */
    public final String getRequestUrl() {
        return apiUrl;
    }

    /**
     * 设置请求URL
     *
     * @param apiUrl
     */
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * 设置请求方式
     *
     * @param method
     */
    public final void setRequestMethod(String method) {
        this.method = method;
    }

    /**
     * @return 拿到请求参数
     */
    protected Object getParams() {
        return param;
    }

    public void setParams(Map<String, Object> param) {
        this.param = param;
    }

    public void setParams(String param) {
        this.param = param;
    }

    /**
     * 拿到请求方式
     *
     * @return
     */
    public final String getRequestMethod() {
        return method;
    }

    public Map<String, Object> getHttpHead() {
        return httpHead;
    }

    public void setHttpHead(Map<String, Object> httpHead) {
        this.httpHead = httpHead;
    }

    public boolean isReadHttpCodeErrorMessage() {
        return isReadHttpCodeErrorMessage;
    }

    public void setReadHttpCodeErrorMessage(boolean isRead) {
        this.isReadHttpCodeErrorMessage = isRead;
    }

    public void addHttpHead(String key, Object value) {
        if (httpHead == null) {
            httpHead = new HashMap<>();
        }
        httpHead.put(key, value);
    }
}
