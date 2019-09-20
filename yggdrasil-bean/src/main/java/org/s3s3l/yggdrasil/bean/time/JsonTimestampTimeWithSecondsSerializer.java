package org.s3s3l.yggdrasil.bean.time;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 时间对象json序列化
 * 
 * @author kehw
 *
 */
public class JsonTimestampTimeWithSecondsSerializer extends JsonSerializer<Timestamp> {

	@Override
	public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		gen.writeString(value.toLocalDateTime().format(formatter));
	}

}
