package io.github.s3s3l.yggdrasil.http;

import java.io.InputStream;
import java.util.Map;

import org.apache.http.entity.ContentType;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * <p>
 * </p>
 * ClassName:HttpHelper <br>
 * Date: Aug 23, 2018 4:43:12 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface HttpHelper {

    HttpHelper basicAuthenticate(String username, String password);

    HttpHelper followRedirects(boolean followRedirects);

    HttpResponse<JsonNode> doPut(String url, String mediaType, ContentType contentType, JsonNode body);

    /**
     * 
     * 发送post请求
     * 
     * @param url
     *            请求地址
     * @param mediaType
     *            媒体类型
     * @param contentType
     *            正文类型
     * @param body
     *            请求体
     * @return response结构体
     * @since JDK 1.8
     */
    HttpResponse<JsonNode> doPost(String url, String mediaType, ContentType contentType, JsonNode body);

    /**
     * 
     * 发送post请求
     * 
     * @param url
     *            请求地址
     * @param mediaType
     *            媒体类型
     * @param contentType
     *            正文类型
     * @param body
     *            请求体
     * @param headers
     *            头信息
     * @return response结构体
     * @since JDK 1.8
     */
    HttpResponse<JsonNode>
            doPost(String url, String mediaType, ContentType contentType, JsonNode body, Map<String, String> headers);

    <T> HttpResponse<T> doPost(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            Class<T> resultType,
            JsonFactory factory);

    <T> HttpResponse<T> doPost(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            Class<T> resultType);

    /**
     * 
     * 发送post请求
     * 
     * @param url
     *            请求地址
     * @param mediaType
     *            媒体类型
     * @param contentType
     *            正文类型
     * @param content
     *            请求体
     * @param headers
     *            头信息
     * @return response结构体
     * @since JDK 1.8
     */
    HttpResponse<JsonNode>
            doPost(String url, String mediaType, ContentType contentType, String content, Map<String, String> headers);

    /**
     * 
     * 发送post请求
     * 
     * @param url
     *            请求地址
     * @param mediaType
     *            媒体类型
     * @param contentType
     *            正文类型
     * @param content
     *            请求体
     * @param headers
     *            头信息
     * @return response结构体
     * @since JDK 1.8
     */
    HttpResponse<JsonNode> doPost(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            JsonFactory factory);

    <T>
            HttpResponse<T>
            doGet(String url, Map<String, Object> content, Map<String, String> headers, Class<T> resultType);

    <T> HttpResponse<T> doGet(String url,
            Map<String, Object> content,
            Map<String, String> headers,
            Class<T> resultType,
            JsonFactory factory);

    HttpResponse<JsonNode> doGet(String url, Map<String, Object> content, Map<String, String> headers);

    HttpResponse<JsonNode>
            doGet(Map<String, String> urlParams, String url, Map<String, Object> content, Map<String, String> headers);

    HttpResponse<JsonNode> doGet(String url, String content, Map<String, String> headers);

    HttpResponse<JsonNode> doGet(String url, String content, Map<String, String> headers, JsonFactory factory);
    
    InputStream getStream(String url);

}
