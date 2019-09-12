package org.s3s3l.yggdrasil.starter.apollo.feature;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.s3s3l.yggdrasil.utils.json.IJacksonHelper;
import org.s3s3l.yggdrasil.utils.json.JacksonUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;

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
public class TreeNodeConverter implements GenericConverter {

    private static final Set<ConvertiblePair> CONVERTIBLE_TYPES;
    private static final IJacksonHelper json = JacksonUtils.create()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    static {
        Set<ConvertiblePair> types = new HashSet<>(1);
        types.add(new ConvertiblePair(TreeNode.class, Object.class));
        types.add(new ConvertiblePair(TreeNode.class, Collection.class));
        CONVERTIBLE_TYPES = Collections.unmodifiableSet(types);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Collections.unmodifiableSet(CONVERTIBLE_TYPES);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        Class<?> sourceClass = sourceType.getType();
        if (source == null || !TreeNode.class.isAssignableFrom(sourceClass) || !(source instanceof TreeNode)) {
            return null;
        }
        if(targetType.isArray()) {
            return json.convert(source, new TypeReference() {
                @Override
                public Type getType() {
                    return targetType.getType();
                }
            });
        }
        return json.treeToValue((TreeNode) source, targetType.getType());
    }
}
