package org.s3s3l.yggdrasil.bean.time;  

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/** 
 * ClassName:JsonDateTimestampDateSerializer <br> 
 * Date:     2016年3月17日 上午11:25:37 <br> 
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public class JsonTimestampDateTimeSerializer extends JsonSerializer<Timestamp> {

	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		gen.writeString(value.toLocalDateTime().format(formatter));
	}
}
  