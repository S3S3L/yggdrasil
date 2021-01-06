package org.s3s3l.yggdrasil.bean.time;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 时间对象反json序列化
 * 
 * @author kehw
 *
 */
public class JsonTimeTimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();

        if (StringUtils.isEmpty(text)) {
            return null;
        } else {
            return Time.valueOf(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm:ss")));
        }
    }

}
