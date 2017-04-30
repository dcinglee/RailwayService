package com.railwayserviceWX.util;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * 这个Https协议工具类，采用HttpsURLConnection实现。
 * 提供get和post两种请求静态方法
 *
 * @author lid
 * @date 2017年2月23日
 */
public class HttpUtil {

    private static TrustManager myX509TrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };

    public static String sendHttpsPOST(String url, String data) {
        String result = null;
        try {
            // 设置SSLContext
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);

            // 打开连接
            // 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
            URL requestUrl = new URL(url);
            HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl.openConnection();

            // 设置套接工厂
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

            // 加入数据
            httpsConn.setRequestMethod("POST");
            httpsConn.setDoOutput(true);
            OutputStream out = httpsConn.getOutputStream();

            if (data != null)
                out.write(data.getBytes("UTF-8"));
            out.flush();
            out.close();

            // 获取输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(), "utf-8"));
            int code = httpsConn.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == code) {
                String temp = in.readLine();
                /* 连接成一个字符串 */
                while (temp != null) {
                    if (result != null)
                        result += temp;
                    else
                        result = temp;
                    temp = in.readLine();
                }
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String sendHttpsGET(String url) {
        String result = null;
        try {
            // 设置SSLContext
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, new TrustManager[]{myX509TrustManager}, null);

            // 打开连接
            // 要发送的POST请求url?Key=Value&amp;Key2=Value2&amp;Key3=Value3的形式
            URL requestUrl = new URL(url);
            HttpsURLConnection httpsConn = (HttpsURLConnection) requestUrl.openConnection();

            // 设置套接工厂
            httpsConn.setSSLSocketFactory(sslcontext.getSocketFactory());

            // 加入数据
            httpsConn.setRequestMethod("GET");
            //httpsConn.setDoOutput(true);

            // 获取输入流
            BufferedReader in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
            int code = httpsConn.getResponseCode();
            if (HttpsURLConnection.HTTP_OK == code) {
                String temp = in.readLine();
                /* 连接成一个字符串 */
                while (temp != null) {
                    if (result != null)
                        result += temp;
                    else
                        result = temp;
                    temp = in.readLine();
                }
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 发送https请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式（GET,POST）
     * @param outputStr     提交的数据
     * @return Map
     * @author lidu
     * @date 2017.2.23
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        try {
            //使用指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod(requestMethod);

            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            //从输入流读取返回结果
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            jsonObject = JSONObject.parseObject(buffer.toString());
        } catch (ConnectException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}	
