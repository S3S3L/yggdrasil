package io.github.s3s3l.yggdrasil.resource.json;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.s3s3l.yggdrasil.resource.DefaultResourceLoader;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;

/**
 * 
 * <p>
 * </p>
 * ClassName: JsonResourceLoader <br>
 * date: Sep 20, 2019 11:25:08 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class JsonResourceLoader extends DefaultResourceLoader {
    public static final List<String> JSON_FILE_EXTENSIONS = Arrays.asList("json", "JSON");

    private JsonResourceLoader() {
        this.parser = JacksonUtils.create(new JsonFactory());
    }

    private JsonResourceLoader(String profileKey) {
        this.parser = JacksonUtils.create(new JsonFactory());
        this.profileKey = profileKey;
    }

    public static JsonResourceLoader create() {
        return new JsonResourceLoader();
    }

    public static JsonResourceLoader create(String profileKey) {
        return new JsonResourceLoader(profileKey);
    }

    public static JsonResourceLoader create(Map<ConfigFeature, Boolean> features) {
        JsonResourceLoader jsonUtil = new JsonResourceLoader();
        jsonUtil.configure(features);
        return jsonUtil;
    }

    public static JsonResourceLoader create(String profileKey, Map<ConfigFeature, Boolean> features) {
        JsonResourceLoader jsonUtil = new JsonResourceLoader(profileKey);
        jsonUtil.configure(features);
        return jsonUtil;
    }
}
