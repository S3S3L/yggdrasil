package io.github.s3s3l.yggdrasil.resource.yaml;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import io.github.s3s3l.yggdrasil.resource.DefaultResourceLoader;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * 
 * <p>
 * </p>
 * ClassName: YAMLResourceLoader <br>
 * date: Sep 20, 2019 11:24:32 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class YAMLResourceLoader extends DefaultResourceLoader {
    public static final List<String> YAML_FILE_EXTENSIONS = Arrays.asList("yaml", "yml", "YAML", "YML");

    private YAMLResourceLoader() {
        this.parser = JacksonUtils.create(new YAMLFactory());
    }

    private YAMLResourceLoader(String profileKey) {
        this.parser = JacksonUtils.create(new YAMLFactory());
        this.profileKey = profileKey;
    }

    public static YAMLResourceLoader create() {
        return new YAMLResourceLoader();
    }

    public static YAMLResourceLoader create(String profileKey) {
        return new YAMLResourceLoader(profileKey);
    }

    public static YAMLResourceLoader create(Map<ConfigFeature, Boolean> features) {
        YAMLResourceLoader yamlUtil = new YAMLResourceLoader();
        yamlUtil.configure(features);
        return yamlUtil;
    }

    public static YAMLResourceLoader create(String profileKey, Map<ConfigFeature, Boolean> features) {
        YAMLResourceLoader yamlUtil = new YAMLResourceLoader(profileKey);
        yamlUtil.configure(features);
        return yamlUtil;
    }

}
