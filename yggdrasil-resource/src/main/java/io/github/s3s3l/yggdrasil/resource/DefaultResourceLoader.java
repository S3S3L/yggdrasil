package io.github.s3s3l.yggdrasil.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import io.github.s3s3l.yggdrasil.utils.common.StringUtils;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.ConfigFeature;

/**
 * 
 * <p>
 * </p>
 * ClassName: DefaultResourceLoader <br>
 * date: Sep 20, 2019 11:25:18 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class DefaultResourceLoader implements ResourceLoader {
    protected static final String SPLITOR = "---";

    protected JacksonHelper parser;
    protected String profileKey;

    /**
     * 
     * setting feature
     * 
     * @param features
     *                 features need to be configured
     * @since JDK 1.8
     */
    public void configure(Map<ConfigFeature, Boolean> features) {
        for (Entry<ConfigFeature, Boolean> entry : features.entrySet()) {
            ConfigFeature feature = entry.getKey();
            Boolean enable = entry.getValue();
            if (feature instanceof SerializationFeature) {
                this.parser.configure((SerializationFeature) feature, enable);
            } else if (feature instanceof DeserializationFeature) {
                this.parser.configure((DeserializationFeature) feature, enable);
            } else if (feature instanceof MapperFeature) {
                this.parser.configure((MapperFeature) feature, enable);
            }
        }
    }

    /**
     * 
     * setting feature
     * 
     * @param feature
     *                feature need to be configured
     * @param enable
     *                if enable feature
     * @since JDK 1.8
     */
    public DefaultResourceLoader configure(ConfigFeature feature, boolean enable) {
        if (feature instanceof SerializationFeature) {
            this.parser.configure((SerializationFeature) feature, enable);
        } else if (feature instanceof DeserializationFeature) {
            this.parser.configure((DeserializationFeature) feature, enable);
        } else if (feature instanceof MapperFeature) {
            this.parser.configure((MapperFeature) feature, enable);
        }

        return this;
    }

    // Non profile

    @Override
    public <T> T loadConfiguration(String resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, resource, type);
    }

    @Override
    public <T> T loadConfiguration(File resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, resource, type);
    }

    @Override
    public <T> T loadConfiguration(Class<T> type, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        return loadConfiguration(type, true, resources);
    }

    @Override
    public <T> T loadConfiguration(Class<T> type, boolean checkFileExist, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, type, checkFileExist, resources);
    }

    @Override
    public <T> T loadConfiguration(Class<T> type, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, type, resources);
    }

    @Override
    public <T> T loadConfiguration(T object, String resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException {
        Verify.notNull(object);
        Verify.hasText(resource);
        Verify.notNull(type);
        for (String subFile : resource.split(SPLITOR)) {
            if (StringUtils.isEmpty(subFile)) {
                continue;
            }

            this.parser.update(object, subFile);
        }

        return object;
    }

    @Override
    public <T> T loadConfiguration(T object, File resource, Class<T> type) throws IOException,
            InstantiationException,
            IllegalAccessException {
        Verify.notNull(object);
        Verify.notNull(resource);
        Verify.notNull(type);
        for (String subFile : FileUtils.splitFile(resource, SPLITOR, StandardCharsets.UTF_8)) {
            if (StringUtils.isEmpty(subFile)) {
                continue;
            }

            this.parser.update(object, subFile);
        }

        return object;
    }

    @Override
    public <T> T loadConfiguration(T object, Class<T> type, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(object);
        Verify.notNull(type);
        Verify.notEmpty(resources);
        return loadConfiguration(object, type, true, resources);
    }

    @Override
    public <T> T loadConfiguration(T object, Class<T> type, boolean checkFileExist, File... resources)
            throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(object);
        Verify.notNull(type);
        Verify.notEmpty(resources);
        for (File resource : resources) {
            try {
                object = this.parser.combine(object, loadConfiguration(resource, type), type);
            } catch (FileNotFoundException e) {
                if (checkFileExist) {
                    throw e;
                }
            }
        }
        return object;
    }

    @Override
    public <T> T loadConfiguration(T object, Class<T> type, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(object);
        Verify.notNull(type);
        Verify.notEmpty(resources);
        for (String resource : resources) {
            object = this.parser.combine(object, loadConfiguration(resource, type), type);
        }
        return object;
    }

    // With profile

    @Override
    public <T> T loadConfiguration(String resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.hasText(resource);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(resource, type);
        }
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, resource, type, profile);
    }

    @Override
    public <T> T loadConfiguration(File resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(resource);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(resource, type);
        }
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, resource, type, profile);
    }

    @Override
    public <T> T loadConfiguration(Class<T> type, String profile, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(resources);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(type, resources);
        }
        return loadConfiguration(type, true, profile, resources);
    }

    @Override
    public <T> T loadConfiguration(Class<T> type, boolean checkFileExist, String profile, File... resources)
            throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(resources);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(type, checkFileExist, resources);
        }
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, type, checkFileExist, profile, resources);
    }

    @Override
    public <T> T loadConfiguration(Class<T> type, String profile, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notEmpty(resources);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(type, resources);
        }
        T result = type.getConstructor().newInstance();
        return loadConfiguration(result, type, profile, resources);
    }

    @Override
    public <T> T loadConfiguration(T object, String resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException {
        Verify.notNull(object);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(object, resource, type);
        }
        boolean firstResource = true;
        for (String subFile : resource.split(SPLITOR)) {
            if (StringUtils.isEmpty(subFile)) {
                continue;
            }
            JsonNode configuration = this.parser.toTreeNode(subFile);

            if (firstResource || StringUtils.isEmpty(profile)
                    || (StringUtils.isEmpty(this.profileKey) && Objects.equals(configuration.get(this.profileKey)
                            .asText(), profile))) {
                this.parser.update(object, subFile);
            }

            if (firstResource) {
                firstResource = false;
            }
        }
        return object;
    }

    @Override
    public <T> T loadConfiguration(T object, File resource, Class<T> type, String profile) throws IOException,
            InstantiationException,
            IllegalAccessException {
        Verify.notNull(object);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(object, resource, type);
        }
        boolean firstResource = true;
        for (String subFile : FileUtils.splitFile(resource, SPLITOR, StandardCharsets.UTF_8)) {
            if (StringUtils.isEmpty(subFile)) {
                continue;
            }
            JsonNode configuration = this.parser.toTreeNode(subFile);

            if (firstResource || StringUtils.isEmpty(profile)
                    || (!StringUtils.isEmpty(this.profileKey) && configuration.has(this.profileKey)
                            && Objects.equals(configuration.get(this.profileKey)
                                    .asText(), profile))) {
                this.parser.update(object, subFile);
            }

            if (firstResource) {
                firstResource = false;
            }
        }
        return object;
    }

    @Override
    public <T> T loadConfiguration(T object, Class<T> type, String profile, File... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(object);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(object, type, resources);
        }
        return loadConfiguration(object, type, true, profile, resources);
    }

    @Override
    public <T> T loadConfiguration(T object, Class<T> type, boolean checkFileExist, String profile, File... resources)
            throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(object);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(object, type, checkFileExist, resources);
        }
        for (File resource : resources) {
            try {
                object = this.parser.combine(object, loadConfiguration(resource, type, profile), type);
            } catch (FileNotFoundException e) {
                if (checkFileExist) {
                    throw e;
                }
            }
        }
        return object;
    }

    @Override
    public <T> T loadConfiguration(T object, Class<T> type, String profile, String... resources) throws IOException,
            InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException,
            SecurityException {
        Verify.notNull(object);
        Verify.notNull(type);
        if (StringUtils.isEmpty(profile)) {
            return loadConfiguration(object, type, resources);
        }
        for (String resource : resources) {
            object = this.parser.combine(object, loadConfiguration(resource, type, profile), type);
        }
        return object;
    }

}
