package io.github.s3s3l.yggdrasil.bean.time;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
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
public class JsonTimestampTimeDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();

        if (StringUtils.isEmpty(text)) {
            return null;
        } else {
            return Timestamp.valueOf(
                    LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm")).atDate(LocalDate.of(1970, 1, 1)));
        }
    }

}
