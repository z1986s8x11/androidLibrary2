package com.zhusx.core.network;

import java.util.List;
import java.util.Map;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/8/22 17:10
 */

public class Response {
    public boolean isSuccess;
    public String error;
    public int code;
    public String body;
    public Map<String, List<String>> header;
}
