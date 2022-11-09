package org.s3s3l.yggdrasil.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.entity.ContentType;
import org.s3s3l.yggdrasil.bean.exception.HttpRequestException;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.stuctural.StructuralHelper;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);
    private static final String ENCODING = "UTF-8";

    public static TreeNode doGet(String url, Map<String, Object> param) throws IOException {
        JacksonHelper json = JacksonUtils.JSON;

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

        logger.debug("doGet. url {}", url);

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
        JacksonHelper json = JacksonUtils.create();

        OkHttpClient client = new OkHttpClient();

        MediaType requestMediaType = MediaType.parse(mediaType);
        RequestBody requestBody = RequestBody.create(requestMediaType,
                body == null ? StringUtils.EMPTY_STRING : json.toStructuralString(body));
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
            StructuralHelper json,
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
}
