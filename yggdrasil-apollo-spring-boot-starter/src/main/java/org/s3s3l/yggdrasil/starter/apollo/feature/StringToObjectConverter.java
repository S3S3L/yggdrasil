package org.s3s3l.yggdrasil.starter.apollo.feature;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonHelper;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * <p>
 * </p>
 * ClassName:TreeNodeConverter <br>
 * Date: Jan 18, 2019 5:09:06 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class StringToObjectConverter implements GenericConverter {

    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;
    private static final Map<String, JacksonHelper> PARSERS = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(getClass());

    static {
        Set<ConvertiblePair> types = new HashSet<>(1);
        types.add(new ConvertiblePair(String.class, Object.class));
        CONVERTIBLE_TYPES = Collections.unmodifiableSet(types);
        PARSERS.put("JSON_PARSER", JacksonUtils.create()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        PARSERS.put("YAML_PARSER", JacksonUtils.create(new YAMLFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        PARSERS.put("XML_PARSER", JacksonUtils.create(new XmlFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
        PARSERS.put("PROPERTIES_PARSER", JacksonUtils.create(new JavaPropsFactory())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.unmodifiableSet(CONVERTIBLE_TYPES);
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Class<?> sourceClass = sourceType.getType();
        if (source == null || !String.class.isAssignableFrom(sourceClass) || !(source instanceof String)) {
            return null;
        }
        return toObject(source, targetType);
    }

    private Object toObject(Object source, TypeDescriptor targetType) {
        for (Entry<String, JacksonHelper> paser : PARSERS.entrySet()) {
            Object result = tryParse((String) source, paser, targetType.getType());
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private Object tryParse(String source, Entry<String, JacksonHelper> helper, Class<?> tr) {
        try {
            return helper.getValue()
                    .toObject(source, tr);
        } catch (Exception e) {
            logger.trace("Source {} can not parse by parser '{}', try next.", source, helper.getKey());
        }

        return null;
    }
}
