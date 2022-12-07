package io.github.s3s3l.yggdrasil.bean.time;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * 日期对象反json序列化
 * 
 * @author kehw
 *
 */
public class JsonTimestampDateDeserializer extends JsonDeserializer<Timestamp> {

    @Override
    public Timestamp deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
        String text = parser.getText();

        if (StringUtils.isEmpty(text)) {
            return null;
        } else {

            return Timestamp.valueOf(LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay());
        }
    }

}
