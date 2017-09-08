package com.zhusx.core.utils;

import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.network.HttpException;
import com.zhusx.core.network.Request;
import com.zhusx.core.network.Response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/8/25 18:03
 */

public class _Request {
    public static Response request(Request request) throws IOException {
        Response response = new Response();
        final String encoding = "UTF-8";
        InputStreamReader bufferReader = null;
        HttpURLConnection urlConn = null;
        if (request.body == null) {
            request.body = "";
        }
        try {
            if (LogUtil.DEBUG) {
                LogUtil.e(String.valueOf(request.url) + "[" + request.method + "]" + "[" + String.valueOf(request.body) + "][" + (request.header == null ? "" : request.header.toString()) + "]");
            }
            URL url = new URL(request.url);
            urlConn = (HttpURLConnection) url.openConnection();
            switch (request.method) {
                case Request.GET:
                    urlConn.setRequestMethod("GET");
                    break;
                case Request.POST:
                    urlConn.setDoInput(true); // 设置输入流采用字节流
                    urlConn.setDoOutput(true); // 设置输出流采用字节流
                    urlConn.setUseCaches(false); // 设置缓存
                    urlConn.setRequestMethod("POST");
                    break;
                case Request.PUT:
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setRequestMethod("PUT");
                    break;
                case Request.DELETE:
                    urlConn.setRequestMethod("DELETE");
                    if (LogUtil.DEBUG) {
                        if (!TextUtils.isEmpty(request.body)) {
                            LogUtil.e("DELETE 不支持提交参数");
                        }
                    }
                    request.body = "";//DELETE 不支持提交参数
                    break;
            }
            byte[] data = request.body.getBytes(encoding);
            //urlConn.setInstanceFollowRedirects(true);//是否连接遵循重定向
            //Content-Type: application/x-www-form-urlencoded   默认的提交方式，同GET类似，将参数组装成Key-value方式，用&分隔，但数据存放在body中提交
            //Content-Type: multipart/form-data                 这种方式一般用来上传文件，或大批量数据时。
            //Content-Type: text/plain                           这种方式一般用来上传字符串。
            if (!TextUtils.isEmpty(request.contentType)) {
                urlConn.setRequestProperty("Content-Type", request.contentType + "; charset=" + encoding);
            }
            urlConn.setRequestProperty("Content-Length", String.valueOf(data.length));
            urlConn.setRequestProperty("Charset", encoding);
            if (request.header != null) {
                for (String key : request.header.keySet()) {
                    Object value = request.header.get(key);
                    urlConn.setRequestProperty(key, value == null ? "" : String.valueOf(value));
                }
            }
            if (request.connectionTimeOut > 0) {
                urlConn.setConnectTimeout(request.connectionTimeOut);
            }
            if (request.readTimeOut > 0) {
                urlConn.setReadTimeout(request.readTimeOut);
            }
            urlConn.connect(); // 连接既往服务端发送消息
            if (data.length > 0) {
                DataOutputStream dop = new DataOutputStream(urlConn.getOutputStream());
                dop.write(data); // 发送参数
                dop.flush(); // 发送，清空缓存
                dop.close(); // 关闭
            }
            response.code = urlConn.getResponseCode();
            if (urlConn.getResponseCode() >= HttpURLConnection.HTTP_OK && urlConn.getResponseCode() < HttpURLConnection.HTTP_MULT_CHOICE) {
                // 下面开始做接收工作
                bufferReader = new InputStreamReader(urlConn.getInputStream());
                StringBuffer sb = new StringBuffer();
                char[] chars = new char[128];
                int length;
                while ((length = bufferReader.read(chars)) != -1) {
                    sb.append(chars, 0, length);
                }
                response.header = urlConn.getHeaderFields();
                response.body = sb.toString();
                response.isSuccess = true;
                if (LogUtil.DEBUG) {
                    LogUtil.e("HTTP Header:" + response.header);
                    LogUtil.e(String.valueOf(response.body));
                }
            } else {
                response.header = urlConn.getHeaderFields();
                if (LogUtil.DEBUG) {
                    LogUtil.e("HTTP CODE:" + urlConn.getResponseCode());
                    LogUtil.e("HTTP Header:" + response.header);
                }
                response.isSuccess = false;
                // 下面开始做接收工作
                InputStream in = urlConn.getErrorStream();
                if (in != null && in.available() >= 0) {
                    bufferReader = new InputStreamReader(in);
                    StringBuffer sb = new StringBuffer();
                    char[] chars = new char[128];
                    int length;
                    while ((length = bufferReader.read(chars)) != -1) {
                        sb.append(chars, 0, length);
                    }
                    response.error = sb.toString();
                    if (LogUtil.DEBUG) {
                        LogUtil.e(String.valueOf(response.error));
                    }
                } else {
                    if (in != null) {
                        in.close();
                    }
                }
            }
        } finally {
            if (urlConn != null) {
                urlConn.disconnect();
            }
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    /**
     * 上传文件到服务器
     *
     * @return 返回响应的内容
     */
    public static Response uploadFile(Request request) throws IOException, HttpException {
        File file = new File(request.body);
        if (!file.exists()) {
            throw new FileNotFoundException("File Not Found Exception!!");
        }
        if (TextUtils.isEmpty(request.contentType)) {
            request.contentType = "image/jpeg";// 流  application/octet-stream
        }
        Response response = new Response();
        String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data"; // 内容类型
        String Charset = "UTF-8";
        URL url = new URL(request.url);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(request.readTimeOut);
        conn.setConnectTimeout(request.connectionTimeOut);
        conn.setDoInput(true); // 允许输入流
        conn.setDoOutput(true); // 允许输出流
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST"); // 请求方式

        if (request.header != null) {
            for (String key : request.header.keySet()) {
                Object value = request.header.get(key);
                conn.setRequestProperty(key, value == null ? "" : String.valueOf(value));
            }
        }
        conn.setRequestProperty("Charset", Charset); // 设置编码
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
        /**
         * 当文件不为空时执行上传
         */
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        StringBuffer sb = new StringBuffer();
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        /**
         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名
         */
        sb.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"" + LINE_END);
        sb.append("Content-Type: " + request.contentType + "; charset=" + Charset + LINE_END);
        sb.append(LINE_END);
        dos.write(sb.toString().getBytes());
        FileInputStream is = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int totalByte = is.available();
        int len;
        int num = 0;
        int progress = 0;
        while ((len = is.read(bytes)) != -1) {
            dos.write(bytes, 0, len);
            if (request.listener != null) {
                if (request.listener.isCanceled()) {
                    throw new HttpException("取消上传");
                }
                num += len;
                int current_progress = totalByte > 0 ? (int) ((float) num / totalByte * 100) : 0;
                if (progress != current_progress) {
                    progress = current_progress;
                    request.listener.onProgress(progress, num, totalByte);
                }
            }
        }
        is.close();
        dos.write(LINE_END.getBytes());
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
        dos.write(end_data);
        dos.flush();
        /**
         * 获取响应码 200=成功 当响应成功，获取响应的流
         */
        response.code = conn.getResponseCode();
        response.header = conn.getHeaderFields();
        if (response.code >= HttpURLConnection.HTTP_OK && response.code < HttpURLConnection.HTTP_MULT_CHOICE) {
            InputStream input = conn.getInputStream();
            sb = new StringBuffer();
            int ss;
            while ((ss = input.read()) != -1) {
                sb.append((char) ss);
            }
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.body = sb.toString();
            if (LogUtil.DEBUG) {
                LogUtil.e("HTTP Header:" + response.header);
                LogUtil.e(String.valueOf(response.body));
            }
            response.isSuccess = true;
        } else {
            InputStream input = conn.getErrorStream();
            if (input != null && input.available() >= 0) {
                InputStreamReader bufferReader = new InputStreamReader(input);
                sb = new StringBuffer();
                char[] chars = new char[128];
                int length;
                while ((length = bufferReader.read(chars)) != -1) {
                    sb.append(chars, 0, length);
                }
                response.error = sb.toString();
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            response.error = "HTTP CODE:" + response.code;
            if (LogUtil.DEBUG) {
                LogUtil.e("HTTP Header:" + response.header);
                LogUtil.e(String.valueOf(response.error));
            }
            response.isSuccess = false;
        }
        return response;
    }

    public static Response downloadFile(Request request) throws HttpException, IOException {
        Response response = new Response();
        InputStream input = null;
        FileOutputStream fos = null;
        HttpURLConnection conn = null;
        RandomAccessFile raf = null;
        if (request.listener != null) {
            if (request.listener.isCanceled()) {
                throw new HttpException(HttpException.ERROR_CODE_CANCEL, "取消下载");
            }
        }
        File file = new File(request.body);
        try {
            if (!file.getParentFile().exists()) {
                if (!file.getParentFile().mkdirs()) {
                    if (LogUtil.DEBUG) {
                        LogUtil.e("创建文件夹失败:"
                                + file.getParentFile().getPath()
                                + "\n检查是否加入android.permission.WRITE_EXTERNAL_STORAGE和android.permission.MOUNT_UNMOUNT_FILESYSTEMS");
                    }
                    throw new HttpException("创建文件夹失败");
                }
            }
            if (file.exists() && Math.abs(System.currentTimeMillis() - file.lastModified()) < request.downloadCacheTime) {
                response.isSuccess = true;
                response.body = request.body;
                return response;
            }
            File fileTemp = new File(file.getPath() + ".tmp");
            int totalByte = 0;
            int progress = -1;
            conn = (HttpURLConnection) new URL(request.url).openConnection();
            // 设置超时
            conn.setConnectTimeout(request.connectionTimeOut);
            // 读取超时 一般不设置
            // conn.setReadTimeout(30000);
            conn.setRequestMethod("GET");
//            // 设置续传开始
            long start = 0;
            if (fileTemp.exists() && Math.abs(System.currentTimeMillis() - fileTemp.lastModified()) < request.downloadCacheTime) {
                raf = new RandomAccessFile(fileTemp, "rw");
                start = raf.length();
                conn.setRequestProperty("Range", "bytes=" + start + "-");
                if (LogUtil.DEBUG) {
                    LogUtil.e("start Range:" + start);
                }
            }
            // 设置方法为 GET
            if (request.listener != null) {
                if (request.listener.isCanceled()) {
                    throw new HttpException(HttpException.ERROR_CODE_CANCEL, "取消下载");
                }
            }
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                if (fileTemp.exists()) {
                    if (!fileTemp.delete()) {
                        if (LogUtil.DEBUG) {
                            LogUtil.e("删除文件失败:" + fileTemp.getPath());
                        }
                        throw new HttpException("删除文件失败");
                    }
                }
                fos = new FileOutputStream(fileTemp);
                String contentLength = conn.getHeaderField("Content-Length");
                if (contentLength == null) {
                    if (LogUtil.DEBUG) {
                        LogUtil.e("Content-Length 文件大小获取失败");
                    }
                } else {
                    totalByte = Integer.parseInt(contentLength);
                }
                input = conn.getInputStream();
                int count = 0;
                int num = 0;
                byte[] b = new byte[1024 * 2];
                while ((count = input.read(b)) != -1) {
                    fos.write(b, 0, count);
                    if (request.listener != null) {
                        if (request.listener.isCanceled()) {
                            throw new HttpException(HttpException.ERROR_CODE_CANCEL, "取消下载");
                        }
                        num += count;
                        int current_progress = totalByte > 0 ? (int) ((float) num / totalByte * 100) : 0;
                        if (progress != current_progress) {
                            progress = current_progress;
                            request.listener.onProgress(progress, num, totalByte);
                        }
                    }
                }
                input.close();
                fos.close();
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                fileTemp.renameTo(file);
                response.body = file.getPath();
                response.isSuccess = true;
                return response;
            } else if (conn.getResponseCode() == HttpURLConnection.HTTP_PARTIAL) {
                if (LogUtil.DEBUG) {
                    LogUtil.e("服务器返回 206");
                }
                raf.seek(start);
                String contentLength = conn.getHeaderField("Content-Length");
                if (contentLength == null) {
                    if (LogUtil.DEBUG) {
                        LogUtil.e("Content-Length 文件大小获取失败");
                    }
                } else {
                    //contentLength 是剩余下载大小的长度
                    totalByte = Integer.parseInt(contentLength) + (int) start;
                }
                input = conn.getInputStream();
                int count = 0;
                int num = (int) start;
                byte[] b = new byte[1024 * 2];
                while ((count = input.read(b)) != -1) {
                    if (request.listener != null) {
                        if (request.listener.isCanceled()) {
                            throw new HttpException(HttpException.ERROR_CODE_CANCEL, "取消下载");
                        }
                        raf.write(b, 0, count);
                        num += count;
                        int current_progress = totalByte > 0 ? (int) ((float) num / totalByte * 100) : 0;
                        if (progress != current_progress) {
                            progress = current_progress;
                            request.listener.onProgress(progress, num, totalByte);
                        }
                    } else {
                        raf.write(b, 0, count);
                    }
                }
                input.close();
                raf.close();
                if (file.exists() && file.isFile()) {
                    file.delete();
                }
                fileTemp.renameTo(file);
                response.body = file.getPath();
                response.isSuccess = true;
                return response;
            } else {
                throw new HttpException(conn.getResponseCode(), "HTTP CODE:" + conn.getResponseCode());
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
