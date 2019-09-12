package org.s3s3l.yggdrasil.utils.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.entity.ContentType;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.s3s3l.yggdrasil.utils.web.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Request.Builder;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

/**
 * <p>
 * </p>
 * ClassName:HttpHelper <br>
 * Date: Jan 4, 2018 2:27:45 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class OkHttpHelper implements HttpHelper {

    private final Logger logger = LoggerFactory.getLogger(OkHttpHelper.class);

    private OkHttpClient client;

    public OkHttpHelper(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public HttpHelper basicAuthenticate(String username, String password) {
        client.setAuthenticator(new Authenticator() {
            @Override
            public Request authenticate(Proxy proxy, Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request()
                        .newBuilder()
                        .header("Authorization", credential)
                        .build();
            }

            @Override
            public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
                return null;
            }
        });
        return this;
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#followRedirects(boolean)
     */
    @Override
    public HttpHelper followRedirects(boolean followRedirects) {
        client.setFollowRedirects(followRedirects);
        return this;
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doPost(java.lang.String,
     *      java.lang.String, org.apache.http.entity.ContentType,
     *      com.fasterxml.jackson.databind.JsonNode)
     */
    @Override
    public HttpResponse<JsonNode> doPost(String url, String mediaType, ContentType contentType, JsonNode body) {
        return doPost(url, mediaType, contentType, body, null);
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doPost(java.lang.String,
     *      java.lang.String, org.apache.http.entity.ContentType,
     *      com.fasterxml.jackson.databind.JsonNode, java.util.Map)
     */
    @Override
    public HttpResponse<JsonNode>
            doPost(String url, String mediaType, ContentType contentType, JsonNode body, Map<String, String> headers) {
        return doPost(url, mediaType, contentType, body.toString(), headers);
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doPost(java.lang.String,
     *      java.lang.String, org.apache.http.entity.ContentType,
     *      java.lang.String, java.util.Map, java.lang.Class,
     *      com.fasterxml.jackson.core.JsonFactory)
     */
    @Override
    public <T> HttpResponse<T> doPost(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            Class<T> resultType,
            JsonFactory factory) {
        HttpResponse<JsonNode> response = doPost(url, mediaType, contentType, content, headers, factory);
        return new HttpResponse<T>().setBody(JacksonUtils.create(factory)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .treeToValue(response.getBody(), resultType))
                .setCode(response.getCode())
                .setHeaders(response.getHeaders());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doPost(java.lang.String,
     *      java.lang.String, org.apache.http.entity.ContentType,
     *      java.lang.String, java.util.Map, java.lang.Class)
     */
    @Override
    public <T> HttpResponse<T> doPost(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            Class<T> resultType) {
        return doPost(url, mediaType, contentType, content, headers, resultType, new JsonFactory());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doPost(java.lang.String,
     *      java.lang.String, org.apache.http.entity.ContentType,
     *      java.lang.String, java.util.Map)
     */
    @Override
    public HttpResponse<JsonNode>
            doPost(String url, String mediaType, ContentType contentType, String content, Map<String, String> headers) {
        return doPost(url, mediaType, contentType, content, headers, new JsonFactory());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doPost(java.lang.String,
     *      java.lang.String, org.apache.http.entity.ContentType,
     *      java.lang.String, java.util.Map,
     *      com.fasterxml.jackson.core.JsonFactory)
     */
    @Override
    public HttpResponse<JsonNode> doPost(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            JsonFactory factory) {

        return doRequest(url, mediaType, contentType, content, headers, factory, HttpMethod.POST);
    }

    private HttpResponse<JsonNode> doRequest(String url,
            String mediaType,
            ContentType contentType,
            String content,
            Map<String, String> headers,
            JsonFactory factory,
            HttpMethod method) {
        MediaType requestMediaType = MediaType.parse(mediaType);
        RequestBody requestBody = RequestBody.create(requestMediaType, content);
        Builder builder = new Request.Builder().url(url);
        switch (method) {
            case GET:
                builder.get();
                break;
            case POST:
                builder.post(requestBody);
                break;
            case PUT:
                builder.put(requestBody);
                break;
            default:
                throw new HttpException(400, "Invalid method");

        }
        if (contentType != null) {
            builder.addHeader("content-type", contentType.getMimeType());
        }
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue()
                        .replaceAll("[\n\r]", ""));
            }
        }

        Request request = builder.build();
        logger.info("doPost: request, {}", request);
        try {
            Response response = client.newCall(request)
                    .execute();
            logger.info("doPost: response, {}", response);
            if (response.code() >= 400) {
                String msg = response.message();
                logger.error("Request fail. Code {}, message {}.", response.code(), msg);
                throw new HttpException(response.code(), "Request fail.");
            }

            try (ResponseBody body = response.body()) {
                return new HttpResponse<JsonNode>().setBody(JacksonUtils.create(factory)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .toTreeNode(body.byteStream()))
                        .setCode(response.code())
                        .setHeaders(response.headers()
                                .toMultimap());
            }
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    // GET

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doGet(java.lang.String,
     *      java.util.Map, java.util.Map, java.lang.Class)
     */
    @Override
    public <T>
            HttpResponse<T>
            doGet(String url, Map<String, Object> content, Map<String, String> headers, Class<T> resultType) {
        return doGet(url, content, headers, resultType, new JsonFactory());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doGet(java.lang.String,
     *      java.util.Map, java.util.Map, java.lang.Class,
     *      com.fasterxml.jackson.core.JsonFactory)
     */
    @Override
    public <T> HttpResponse<T> doGet(String url,
            Map<String, Object> content,
            Map<String, String> headers,
            Class<T> resultType,
            JsonFactory factory) {
        HttpResponse<JsonNode> response = doGet(url, content == null ? null : UrlUtils.buildRequestParam(content),
                headers, factory);
        return new HttpResponse<T>().setBody(JacksonUtils.create(factory)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .treeToValue(response.getBody(), resultType))
                .setCode(response.getCode())
                .setHeaders(response.getHeaders());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doGet(java.lang.String,
     *      java.util.Map, java.util.Map)
     */
    @Override
    public HttpResponse<JsonNode> doGet(String url, Map<String, Object> content, Map<String, String> headers) {
        return doGet(url, content == null ? null : UrlUtils.buildRequestParam(content), headers, new JsonFactory());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doGet(java.util.Map,
     *      java.lang.String, java.util.Map, java.util.Map)
     */
    @Override
    public HttpResponse<JsonNode>
            doGet(Map<String, String> urlParams, String url, Map<String, Object> content, Map<String, String> headers) {
        return doGet(UrlUtils.buildUrl(url, urlParams), content == null ? null : UrlUtils.buildRequestParam(content),
                headers, new JsonFactory());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doGet(java.lang.String,
     *      java.lang.String, java.util.Map)
     */
    @Override
    public HttpResponse<JsonNode> doGet(String url, String content, Map<String, String> headers) {
        return doGet(url, content, headers, new JsonFactory());
    }

    /**
     * @see org.s3s3l.yggdrasil.utils.http.HttpHelper#doGet(java.lang.String,
     *      java.lang.String, java.util.Map,
     *      com.fasterxml.jackson.core.JsonFactory)
     */
    @Override
    public HttpResponse<JsonNode> doGet(String url, String content, Map<String, String> headers, JsonFactory factory) {

        Builder builder = new Request.Builder().url(StringUtils.isEmpty(content) ? url : String.join("?", url, content))
                .get();
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue()
                        .replaceAll("[\n\r]", ""));
            }
        }

        Request request = builder.build();
        logger.debug("doGet: request, {}", request);
        try {
            Response response = client.newCall(request)
                    .execute();
            logger.debug("doGet: response, {}", response);
            if (response.code() >= 400) {
                String msg = response.message();
                logger.error("Request fail. Code {}, message {}.", response.code(), msg);
                throw new HttpException(response.code(), "Request fail.");
            }

            try (ResponseBody body = response.body()) {
                return new HttpResponse<JsonNode>().setBody(JacksonUtils.create(factory)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .toTreeNode(body.byteStream()))
                        .setCode(response.code())
                        .setHeaders(response.headers()
                                .toMultimap());
            }
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }

    @Override
    public HttpResponse<JsonNode> doPut(String url, String mediaType, ContentType contentType, JsonNode body) {
        return doRequest(url, mediaType, contentType, body.toString(), null, new JsonFactory(), HttpMethod.PUT);
    }

    @Override
    public InputStream getStream(String url) {
        try {
            Response response = client.newCall(new Builder().url(url)
                    .build())
                    .execute();
            if(response.code() >= 400) {
                String msg = response.message();
                logger.error("Request fail. Code {}, message {}.", response.code(), msg);
                throw new HttpException(response.code(), "Request fail.");
            }
            return response
                    .body()
                    .byteStream();
        } catch (IOException e) {
            throw new HttpException(e);
        }
    }
}
