package io.github.s3s3l.yggdrasil.bean.time;  

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/** 
 * ClassName:JsonDateTimestampDateDeserializer <br> 
 * Date:     2016年3月17日 上午11:23:39 <br> 
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class JsonTimestampDateTimeDeserializer extends JsonDeserializer<Timestamp> {

	@Override
	public Timestamp deserialize(JsonParser parser, DeserializationContext ctx) throws IOException {
		String text = parser.getText();

		if (StringUtils.isEmpty(text)) {
			return null;
		} else {
            return Timestamp.valueOf(LocalDateTime.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
		}
	}
}
  