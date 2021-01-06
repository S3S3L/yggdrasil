package org.s3s3l.yggdrasil.bean.time;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

/**
 * 时间对象json序列化
 *
 * @author kehw
 */
public class JsonTimeTimeSerializer extends JsonSerializer<Time> {

    @Override
    public void serialize(Time value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        gen.writeString(value.toLocalTime().format(formatter));
    }

}
