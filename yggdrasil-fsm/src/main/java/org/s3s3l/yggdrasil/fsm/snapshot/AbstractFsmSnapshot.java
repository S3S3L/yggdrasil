package org.s3s3l.yggdrasil.fsm.snapshot;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.s3s3l.yggdrasil.fsm.AbstractFsm;
import org.s3s3l.yggdrasil.persistence.snapshot.ByteArraySnapshot;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 3:32:31 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class AbstractFsmSnapshot<T extends AbstractFsm> extends VersioningSnapshot
        implements ByteArraySnapshot<T> {

    @Override
    public Map<byte[], byte[]> toMap() {
        Map<byte[], byte[]> map = new HashMap<>();
        Class<?> type = getClass();
        ReflectionBean reflection = new PropertyDescriptorReflectionBean(this);
        for (Field field : ReflectionUtils.getFields(type)) {
            String fieldName = field.getName();
            if (ByteArraySnapshot.class.isAssignableFrom(field.getType())) {
                map.put(fieldName.getBytes(StandardCharsets.UTF_8), JacksonUtils.NONNULL_JSON
                        .toStructuralBytes(((ByteArraySnapshot<?>) reflection.getFieldValue(fieldName)).toMap()));
            } else {
                map.put(fieldName.getBytes(StandardCharsets.UTF_8),
                        JacksonUtils.NONNULL_JSON.toStructuralBytes(reflection.getFieldValue(fieldName)));
            }
        }
        return map;
    }
}
