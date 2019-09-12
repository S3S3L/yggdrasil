package org.s3s3l.yggdrasil.utils.http;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * </p>
 * ClassName:HttpResponse <br>
 * Date: May 23, 2019 9:49:17 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class HttpResponse<T> {
    private T body;
    private Map<String, List<String>> headers;
    private int code;

    public T getBody() {
        return body;
    }

    public HttpResponse<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public HttpResponse<T> setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
        return this;
    }

    public int getCode() {
        return code;
    }

    public HttpResponse<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public boolean isSuccess() {
        return this.code < 400;
    }
}
