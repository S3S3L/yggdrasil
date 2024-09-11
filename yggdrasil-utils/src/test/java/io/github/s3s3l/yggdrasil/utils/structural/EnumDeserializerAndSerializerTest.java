package io.github.s3s3l.yggdrasil.utils.structural;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;

import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.EnumDeserializer;
import io.github.s3s3l.yggdrasil.utils.stuctural.jackson.EnumSerializer;
import lombok.Data;

public class EnumDeserializerAndSerializerTest {

    public enum TestEnum {
        VALUE1, VALUE2
    }

    @Data
    private static class TestClass {
        public TestEnum testEnum;
        public String nonEnumField;
    }

    @Test
    public void testDeserializeValidEnum() throws IOException {
        EnumDeserializer deserializer = new EnumDeserializer();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getCurrentName()).thenReturn("testEnum");
        Mockito.when(jsonParser.getCurrentValue()).thenReturn(new TestClass());
        Mockito.when(jsonParser.getText()).thenReturn("VALUE1");

        @SuppressWarnings("rawtypes")
        Enum result = deserializer.deserialize(jsonParser, ctxt);
        Assertions.assertEquals(TestEnum.VALUE1, result);
    }

    @Test
    public void testDeserializeInvalidEnum() throws IOException {
        EnumDeserializer deserializer = new EnumDeserializer();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getCurrentName()).thenReturn("testEnum");
        Mockito.when(jsonParser.getCurrentValue()).thenReturn(new TestClass());
        Mockito.when(jsonParser.getText()).thenReturn("INVALID");

        @SuppressWarnings("rawtypes")
        Enum result = deserializer.deserialize(jsonParser, ctxt);
        Assertions.assertNull(result);
    }

    @Test
    public void testDeserializeNonEnumField() throws IOException {
        EnumDeserializer deserializer = new EnumDeserializer();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getCurrentName()).thenReturn("nonEnumField");
        Mockito.when(jsonParser.getCurrentValue()).thenReturn(new TestClass());
        Mockito.when(jsonParser.getText()).thenReturn("VALUE1");

        @SuppressWarnings("rawtypes")
        Enum result = deserializer.deserialize(jsonParser, ctxt);
        Assertions.assertNull(result);
    }

    @Test
    public void testDeserializeFieldNotFound() throws IOException {
        EnumDeserializer deserializer = new EnumDeserializer();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext ctxt = Mockito.mock(DeserializationContext.class);

        Mockito.when(jsonParser.getCurrentName()).thenReturn("nonExistentField");
        Mockito.when(jsonParser.getCurrentValue()).thenReturn(new TestClass());
        Mockito.when(jsonParser.getText()).thenReturn("VALUE1");

        @SuppressWarnings("rawtypes")
        Enum result = deserializer.deserialize(jsonParser, ctxt);
        Assertions.assertNull(result);
    }

    @Test
    public void testFindField() {
        EnumDeserializer deserializer = new EnumDeserializer();
        Field field = deserializer.findField("testEnum", TestClass.class);
        Assertions.assertNotNull(field);
        Assertions.assertEquals("testEnum", field.getName());

        field = deserializer.findField("nonExistentField", TestClass.class);
        Assertions.assertNull(field);
    }

    @Test
    public void testSerialize() throws IOException {
        EnumSerializer serializer = new EnumSerializer();
        StringWriter writer = new StringWriter();
        JsonGenerator gen = new JsonMapper().getFactory().createGenerator(writer);
        SerializerProvider provider = new ObjectMapper().getSerializerProvider();

        serializer.serialize(TestEnum.VALUE1, gen, provider);
        gen.flush();

        assertEquals("\"VALUE1\"", writer.toString());
    }
}