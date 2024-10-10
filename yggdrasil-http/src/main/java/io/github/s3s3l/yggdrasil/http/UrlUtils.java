package io.github.s3s3l.yggdrasil.http;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.StructuralHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * <p>
 * </p>
 * ClassName:UrlUtils <br>
 * Date: Nov 21, 2016 6:51:09 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class UrlUtils {
    private static final Pattern URL_SPLITOR_PATTERN = Pattern.compile("\\{.*\\}");

    /**
     * 
     * 获取指定的url参数的json对象
     * 
     * @param key
     *            参数名
     * @param requestParam
     *            requestParam的json对象
     * @return json对象
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static JsonNode getField(String key, ObjectNode requestParam) {
        if (!StringUtils.isMatch(key,
                "^[_a-zA-Z][_a-zA-Z\\d]*(\\[[_a-zA-Z][_a-zA-Z\\d]*\\])*(\\[([_a-zA-Z][_a-zA-Z\\d]*)?\\])?$")) {
            return null;
        }
        QueryStringKey qsKey = new QueryStringKey(key);
        ObjectNode currentNode = requestParam;
        while (qsKey.hasNext()) {
            String currentKey = qsKey.nextkey();
            if (!qsKey.hasNext()) {
                return currentNode.get(currentKey);
            }

            currentNode = (ObjectNode) currentNode.get(currentKey);
        }

        return null;
    }

    /**
     * 
     * 获取指定的url参数的指定类型对象
     * 
     * @param key
     *            参数名
     * @param requestParam
     *            requestParam的json对象
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 指定类型对象
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static <T> T getField(String key, ObjectNode requestParam, TypeReference<T> type) {
        StructuralHelper json = JacksonUtils.create();
        JsonNode param = getField(key, requestParam);
        return json.toObject(param == null ? StringUtils.NULL_STRING : param.toString(), type);
    }

    /**
     * 
     * 获取指定的url参数的指定类型对象
     * 
     * @param key
     *            参数名
     * @param requestParam
     *            requestParam的json对象
     * @param cls
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 指定类型对象
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static <T> T getField(String key, ObjectNode requestParam, Class<T> cls) {
        return getField(key, requestParam, new TypeReference<T>() {
            @Override
            public Type getType() {
                return cls;
            }
        });
    }

    /**
     * 
     * 转化url参数到对象
     * 
     * @param uri
     *            完整url
     * @return json对象
     * @since JDK 1.8
     */
    @SuppressWarnings("unchecked")
    public static ObjectNode getRequestParam(URI uri) {
        JacksonHelper json = JacksonUtils.create()
                .prettyPrinter();
        List<NameValuePair> params = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);

        Map<String, Object> fields = new HashMap<>();
        for (NameValuePair param : params) {
            String key = param.getName();
            if (fields.containsKey(param.getName())) {
                Object oldValue = fields.get(key);
                if (oldValue instanceof List) {
                    ((List<Object>) oldValue).add(param.getValue());
                    fields.put(key, oldValue);

                } else {
                    fields.put(key, new ArrayList<Object>(Arrays.asList(oldValue, param.getValue())));

                }
            } else {
                fields.put(param.getName(), param.getValue());
            }
        }

        ObjectNode jsonNode = json.createObjectNode();

        for (Entry<String, Object> entry : fields.entrySet()) {
            String key = entry.getKey();
            QueryStringKey qsKey = new QueryStringKey(key);
            ObjectNode currentNode = jsonNode;
            while (qsKey.hasNext()) {
                String currentKey = qsKey.nextkey();
                if (!qsKey.hasNext()) {
                    if (qsKey.isArray()) {
                        currentNode.set(currentKey, json.convert(fields.get(key), ArrayNode.class));
                    } else {
                        currentNode.put(currentKey, fields.get(key)
                                .toString());
                    }
                    continue;
                } else if (!currentNode.has(currentKey)) {
                    currentNode.putObject(currentKey);
                }

                currentNode = (ObjectNode) currentNode.get(currentKey);
            }
        }

        return jsonNode;
    }

    /**
     * 
     * 转化url参数到对象
     * 
     * @param uri
     *            完整url
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 指定类型的对象
     * @since JDK 1.8
     */
    public static <T> T getRequestParam(URI uri, TypeReference<T> type) {
        JacksonHelper json = JacksonUtils.create();
        return json.convert(getRequestParam(uri), type);
    }

    /**
     * 
     * 转化url参数到对象
     * 
     * @param uri
     *            完整url
     * @param cls
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 指定类型的对象
     * @since JDK 1.8
     */
    public static <T> T getRequestParam(URI uri, Class<T> cls) {
        Verify.notNull(cls);
        return getRequestParam(uri, new TypeReference<T>() {
        });
    }

    /**
     * 
     * 转化url参数到对象
     * 
     * @param url
     *            完整url
     * @return json对象
     * @throws URISyntaxException
     *             {@link URISyntaxException}
     * @since JDK 1.8
     */
    public static ObjectNode getRequestParam(String url) throws URISyntaxException {
        return getRequestParam(new URI(url));
    }

    /**
     * 
     * 转化url参数到对象
     * 
     * @param url
     *            完整url
     * @param type
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 指定类型的对象
     * @throws URISyntaxException
     *             {@link URISyntaxException}
     * @since JDK 1.8
     */
    public static <T> T getRequestParam(String url, TypeReference<T> type) throws URISyntaxException {
        JacksonHelper json = JacksonUtils.create();
        return json.convert(getRequestParam(url), type);
    }

    /**
     * 
     * 转化url参数到对象
     * 
     * @param url
     *            完整url
     * @param cls
     *            目标类型
     * @param <T>
     *            目标类型
     * @return 指定类型的对象
     * @throws URISyntaxException
     *             {@link URISyntaxException}
     * @since JDK 1.8
     */
    public static <T> T getRequestParam(String url, Class<T> cls) throws URISyntaxException {
        Verify.notNull(cls);
        return getRequestParam(url, new TypeReference<T>() {
        });
    }

    public static String buildRequestParam(Map<String, Object> param) {
        List<String> params = new ArrayList<>();
        for (Entry<String, Object> entry : param.entrySet()) {
            if (entry.getValue() == null) {
                continue;
            }
            params.add(String.join("=", entry.getKey(), entry.getValue()
                    .toString()));
        }
        return String.join("&", params);
    }

    public static String buildUrl(String url, Map<String, String> param) {
        return String.join("/", Arrays.stream(url.split("/"))
                .map(r -> {
                    if (!URL_SPLITOR_PATTERN.matcher(r)
                            .matches()) {
                        return r;
                    }
                    String paramName = r.substring(1, r.length() - 1);
                    if (!param.containsKey(paramName)) {
                        return r;
                    }
                    return param.get(paramName);
                })
                .collect(Collectors.toList()));
    }

}
