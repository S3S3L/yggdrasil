package org.s3s3l.yggdrasil.utils.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.s3s3l.yggdrasil.bean.exception.HttpRequestException;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.json.IJacksonHelper;
import org.s3s3l.yggdrasil.utils.json.IJsonHelper;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.s3s3l.yggdrasil.utils.log.base.LogHelper;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

/**
 * ClassName:HttpUtils <br>
 * Date: 2016年1月13日 下午4:26:25 <br>
 * 
 * @author kehw_zwei
 * @version 1.0
 * @since JDK 1.8
 */
public abstract class HttpUtils {
    private static final String ENCODING = "UTF-8";

    public static TreeNode doGet(String url, Map<String, Object> param) throws IOException {
        IJacksonHelper json = JacksonUtils.create();

        OkHttpClient client = new OkHttpClient();

        List<String> params = new ArrayList<>();
        for (Entry<String, Object> entry : param.entrySet()) {
            params.add(entry.getKey()
                    .concat("=")
                    .concat(entry.getValue()
                            .toString()));
        }
        url = url.concat("?")
                .concat(String.join("&", params));
        Request request = new Request.Builder().url(url)
                .get()
                .build();

        LogHelper.getLogger(HttpUtils.class)
                .info("doGet. url {}", url);

        Response response = client.newCall(request)
                .execute();

        if (!response.isSuccessful()) {
            throw new HttpRequestException(response.code(), response.message());
        }

        return json.toTreeNode(response.body()
                .byteStream());
    }

    public static TreeNode doPost(String url, String mediaType, ContentType contentType, Map<String, Object> body)
            throws IOException {
        IJacksonHelper json = JacksonUtils.create();

        OkHttpClient client = new OkHttpClient();

        MediaType requestMediaType = MediaType.parse(mediaType);
        RequestBody requestBody = RequestBody.create(requestMediaType,
                body == null ? StringUtils.EMPTY_STRING : json.toJsonString(body));
        Request request = new Request.Builder().url(url)
                .post(requestBody)
                .addHeader("content-type", contentType.getMimeType())
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request)
                .execute();

        if (!response.isSuccessful()) {
            throw new HttpRequestException(response.code(), response.message());
        }

        return json.toTreeNode(response.body()
                .byteStream());
    }

    public static <T> T doPost(String url,
            String mediaType,
            ContentType contentType,
            Map<String, Object> body,
            String dataField,
            IJsonHelper json,
            TypeReference<T> resultType) throws IOException {
        TreeNode responseBody = doPost(url, mediaType, contentType, body);

        return json.toObject(responseBody.get(dataField)
                .toString(), resultType);
    }

    public static String sendPost(String url, Map<String, String> data) throws IOException {
        URL realUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.connect();

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(String.format("%s=%s", entry.getKey(), URLEncoder.encode(entry.getValue(), ENCODING)));
        }

        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.writeBytes(stringBuilder.toString());
        out.flush();
        out.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String lines;
        StringBuilder sb = new StringBuilder("");
        while ((lines = reader.readLine()) != null) {
            lines = new String(lines.getBytes(), "utf-8");
            sb.append(lines);
        }
        reader.close();
        connection.disconnect();
        return sb.toString();
    }

    /**
     * Http Post操作
     * 
     * @param url
     * @param body
     * @return
     * @throws IOException
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public static JSONObject doPost(String url, Map<String, String> headers, String params, ContentType contentType)
            throws IOException {
        JSONObject result = null;
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            StringEntity entity = new StringEntity(params, contentType);
            entity.setContentEncoding(ENCODING);
            httpPost.setEntity(entity);
            headers.entrySet()
                    .forEach(header -> httpPost.addHeader(header.getKey(), header.getValue()));
            CloseableHttpResponse response = client.execute(httpPost);
            InputStreamReader reader = new InputStreamReader(response.getEntity()
                    .getContent());
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1];
            while (reader.read(buf) != -1) {
                sb.append(new String(buf));
            }
            result = (JSONObject) JSONObject.parse(sb.toString());
        }
        return result;
    }

    /**
     * Http Post操作
     * 
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    public static JSONObject doPost(String url, String params) throws IOException {
        JSONObject result = null;
        HttpPost httpPost = new HttpPost(url);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            StringEntity entity = new StringEntity(params);
            entity.setContentType("application/json");
            entity.setContentEncoding(ENCODING);
            httpPost.setEntity(entity);
            CloseableHttpResponse response = client.execute(httpPost);
            InputStreamReader reader = new InputStreamReader(response.getEntity()
                    .getContent());
            StringBuilder sb = new StringBuilder();
            char[] buf = new char[1];
            while (reader.read(buf) != -1) {
                sb.append(new String(buf));
            }
            result = (JSONObject) JSONObject.parse(sb.toString());
        }
        return result;
    }
}
