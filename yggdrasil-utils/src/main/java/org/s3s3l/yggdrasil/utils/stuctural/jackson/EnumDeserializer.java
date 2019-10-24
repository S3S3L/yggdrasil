package org.s3s3l.yggdrasil.utils.stuctural.jackson;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.s3s3l.yggdrasil.utils.common.EnumParser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * <p>
 * </p>
 * ClassName:EnumDeserializer <br>
 * Date: Dec 28, 2018 3:29:55 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@SuppressWarnings("rawtypes")
public class EnumDeserializer extends JsonDeserializer<Enum> {

    @SuppressWarnings("unchecked")
    @Override
    public Enum deserialize(JsonParser jsonparser, DeserializationContext ctxt) throws IOException {
        Field field = findField(jsonparser.getCurrentName(), jsonparser.getCurrentValue()
                .getClass());
        if (field == null) {
            return null;
        }

        Class<?> fieldType = field.getType();
        if (!Enum.class.isAssignableFrom(fieldType)) {
            return null;
        }

        return EnumParser.parse(jsonparser.getText(), (Class<? extends Enum>) fieldType);
    }

    public Field findField(String name, Class<?> c) {
        for (; c != null; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                if (field.getName()
                        .equals(name)) {
                    return field;
                }
            }
        }
        return null;
    }
}
