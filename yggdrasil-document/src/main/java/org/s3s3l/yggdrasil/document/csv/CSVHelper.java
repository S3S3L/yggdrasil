package org.s3s3l.yggdrasil.document.csv;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.bean.exception.ResourceLoadingException;
import org.s3s3l.yggdrasil.document.ResourceMapper;
import org.s3s3l.yggdrasil.document.excel.FieldMap;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.common.TypeUtils;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import com.fasterxml.jackson.databind.JsonNode;
import com.opencsv.CSVParser;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

/**
 * <p>
 * </p>
 * ClassName:CSVHelper <br>
 * Date: Dec 26, 2017 3:47:11 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CSVHelper implements ResourceMapper {

    /**
     * row limit in one sheet
     */
    private static final int MAX_ROW_COUNT = 100000;
    private final CSVReaderBuilder builder;

    private StringBuilder content ;

    private CSVHelper(InputStream is) {
        Verify.notNull(is);
        builder = new CSVReaderBuilder(new InputStreamReader(is)).withCSVParser(new CSVParser());
    }

    public static ResourceMapper create(InputStream is) {
        return new CSVHelper(is);
    }

    @Override
    public <T> List<T> mapList(int columnIndex, Class<T> type) {
        Verify.largerThan(columnIndex, 0);
        Verify.notNull(type);

        List<T> result = new ArrayList<>();
        int index = columnIndex - 1;
        try (CSVReader reader = builder.build()) {
            reader.forEach(r -> {
                if (r.length < columnIndex) {
                    throw new ResourceLoadingException(String.format(
                            "Required column '%d' is out of index, max column index is '%d'", columnIndex, r.length));
                }
                result.add(TypeUtils.fromString(r[index], type));
            });
        } catch (IOException e) {
            throw new ResourceLoadingException(e);
        }
        return result;
    }

    @Override
    public <T> List<T> mapList(int columnIndex, int offsetRow, Class<T> type) {
        Verify.largerThan(columnIndex, 0);
        Verify.notLessThan(columnIndex, 0);
        Verify.notNull(type);

        List<T> result = new ArrayList<>();
        int index = columnIndex - 1;
        AtomicInteger currentLine = new AtomicInteger(1);
        try (CSVReader reader = builder.build()) {
            reader.forEach(r -> {
                if (r.length < columnIndex) {
                    throw new ResourceLoadingException(String.format(
                            "Required column '%d' is out of index, max column index is '%d'", columnIndex, r.length));
                }
                if (currentLine.getAndIncrement() > offsetRow) {
                    result.add(TypeUtils.fromString(r[index], type));
                }
            });
        } catch (IOException e) {
            throw new ResourceLoadingException(e);
        }
        return result;
    }

    public <T> CSVHelper buildCSVFile(List<T> objList, Class<T> cls) {

        if (objList == null || objList.isEmpty()) {
            return this;
        }

        List<Field> fields = ReflectionUtils.getFields(cls).stream().filter(r -> r.isAnnotationPresent(FieldMap.class))
                .sorted((r, s) -> r.getAnnotation(FieldMap.class).order() - s.getAnnotation(FieldMap.class).order())
                .collect(Collectors.toList());

        content = new StringBuilder("");

        if (MAX_ROW_COUNT > objList.size()) {
            // 创建标题行
            for (Field field : fields) {
                FieldMap fieldMap = field.getAnnotation(FieldMap.class);
                String columnName = StringUtils.isEmpty(fieldMap.value()) ? field.getName() : fieldMap.value();
                content.append(columnName).append(",");
            }

            // 去掉最后的逗号，并换行
            content.deleteCharAt(content.length() - 1).append("\n");

            // 拼接csv的正文内容
            for(int i = 0; i < objList.size(); i++){
                T obj = objList.get(i);
                JsonNode rowValue = JacksonUtils.defaultHelper.valueToTree(obj);
                for (Field field : fields) {
                    FieldMap fieldMap = field.getAnnotation(FieldMap.class);
                    String fieldName = field.getName();

                    // 设置正文内容
                    content.append(rowValue.hasNonNull(fieldName)
                                    ? String.join(StringUtils.EMPTY_STRING, fieldMap.prefix(),
                                    rowValue.get(fieldName).asText(), fieldMap.suffix())
                                    : StringUtils.EMPTY_STRING).append(",");
                }
                // 去掉最后的逗号，并换行
                content.deleteCharAt(content.length() - 1).append("\n");
            }
        }else{
            throw new IllegalArgumentException("the count of the record is exceeding the max row limit");
        }
        return this;
    }

    public void saveAS(OutputStream os) throws IOException {
        os.write(content.toString().getBytes(StandardCharsets.UTF_8));
    }
}
