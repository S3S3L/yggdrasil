package org.s3s3l.yggdrasil.resource.prop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.s3s3l.yggdrasil.resource.DefaultResourceLoader;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import com.fasterxml.jackson.databind.cfg.ConfigFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;

/**
 * 
 * <p>
 * </p>
 * ClassName: PropertyResourceLoader <br>
 * date: Sep 20, 2019 11:24:58 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class PropertyResourceLoader extends DefaultResourceLoader {
    public static final List<String> PROP_FILE_EXTENSIONS = Arrays.asList("properties", "PROPERTIES");

    private PropertyResourceLoader() {
        this.parser = JacksonUtils.create(new JavaPropsFactory());
    }

    private PropertyResourceLoader(String profileKey) {
        this.parser = JacksonUtils.create(new JavaPropsFactory());
        this.profileKey = profileKey;
    }

    public static PropertyResourceLoader create() {
        return new PropertyResourceLoader();
    }

    public static PropertyResourceLoader create(String profileKey) {
        return new PropertyResourceLoader(profileKey);
    }

    public static PropertyResourceLoader create(Map<ConfigFeature, Boolean> features) {
        PropertyResourceLoader propertyUtil = new PropertyResourceLoader();
        propertyUtil.configure(features);
        return propertyUtil;
    }

    public static PropertyResourceLoader create(String profileKey, Map<ConfigFeature, Boolean> features) {
        PropertyResourceLoader propertyUtil = new PropertyResourceLoader(profileKey);
        propertyUtil.configure(features);
        return propertyUtil;
    }

    /**
     * 
     * 获取配置
     * 
     * @param configFilePath
     *            配置文件路径
     * @return Properties对象
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static Properties getConfig(String configFilePath) throws IOException {

        try (InputStream inputStream = new FileInputStream(configFilePath)) {
            Properties config = new Properties();
            config.load(inputStream);
            return config;
        }
    }

    /**
     * 
     * 获取配置
     * 
     * @param configFilePath
     *            配置文件路径
     * @return Properties对象
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static Properties getConfig(String profileKey, String profile, String configFilePath) throws IOException {
        Verify.hasText(profileKey);

        Properties prop = getConfig(configFilePath);
        if (StringUtils.isEmpty(profile)) {
            return prop;
        }

        Set<Entry<Object, Object>> entrySet = new HashSet<>(prop.entrySet());

        for (Entry<Object, Object> entry : entrySet) {
            String key = entry.getKey()
                    .toString();
            int index = key.indexOf('.');

            if (index < 0) {
                continue;
            }

            String value = entry.getValue()
                    .toString();
            prop.remove(entry);

            if (key.substring(0, index)
                    .equals(profile) && key.length() > index + 1) {
                String newKey = key.substring(index + 1);
                prop.setProperty(newKey, value);
            }
        }

        return prop;
    }
}
