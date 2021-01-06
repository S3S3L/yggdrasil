package org.s3s3l.yggdrasil.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.s3s3l.yggdrasil.document.excel.FieldMap;
import org.s3s3l.yggdrasil.document.excel.WorkbookBuilder;
import org.s3s3l.yggdrasil.document.excel.enumerations.WorkbookType;
import org.s3s3l.yggdrasil.document.excel.exception.WorkbookException;
import org.s3s3l.yggdrasil.document.excel.handler.CellStyleBuilder;
import org.s3s3l.yggdrasil.document.excel.handler.FontBuilder;
import org.s3s3l.yggdrasil.document.excel.handler.impl.POICellStyleBuilder;
import org.s3s3l.yggdrasil.document.excel.handler.impl.POIFontBuilder;
import org.s3s3l.yggdrasil.utils.common.StringUtils;
import org.s3s3l.yggdrasil.utils.common.TypeUtils;
import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import org.s3s3l.yggdrasil.utils.stuctural.jackson.JacksonUtils;

/**
 * <p>
 * </p>
 * ClassName:POIUtils <br>
 * Date: Nov 14, 2016 5:25:46 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class POIUtils implements WorkbookBuilder {

    private static final String MSG_WORK_BOOK_NOT_SET = "field \"workbook\" not set.";
    private static final String MSG_WORK_BOOK_IS_NULL = "\"workbook\" is null.";

    /**
     * row limit in one sheet
     */
    private static final int MAX_ROW_COUNT = 100000;

    private Workbook workbook;

    private POIUtils() {
    }

    /**
     * 
     * 获取实例
     * 
     * @param workbookType
     *            工作簿类型
     * @return 工具类实例
     * @since JDK 1.8
     */
    public static WorkbookBuilder create(WorkbookType workbookType) {
        POIUtils poi = new POIUtils();
        poi.initWorkbook(workbookType);

        return poi;
    }

    /**
     * 
     * 获取实例
     * 
     * @param path
     *            工作簿路径
     * @return 工具类实例
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static WorkbookBuilder create(String path) throws IOException {
        POIUtils poi = new POIUtils();
        poi.loadWorkbook(path);

        return poi;
    }

    /**
     * 
     * 获取实例
     * 
     * @param path
     *            工作簿路径
     * @return 工具类实例
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static WorkbookBuilder create(Path path) throws IOException {
        return create(path.toString());
    }

    /**
     * 
     * 获取实例
     * 
     * @param file
     *            工作簿文件
     * @return 工具类实例
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static WorkbookBuilder create(File file) throws IOException {
        return create(file.toString());
    }

    /**
     * 
     * 获取实例
     * 
     * @param file
     *            工作簿文件
     * @return 工具类实例
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public static WorkbookBuilder create(InputStream file, WorkbookType workbookType) throws IOException {
        POIUtils poi = new POIUtils();
        poi.loadWorkbook(file, workbookType);

        return poi;
    }

    @Override
    public FontBuilder getFontBuilder() {
        return POIFontBuilder.create(createFont());
    }

    @Override
    public CellStyleBuilder getCellStyleBuilder() {
        return POICellStyleBuilder.create(createCellStyle());

    }

    @Override
    public WorkbookBuilder buildSheet(List<?> objList,
            Map<String, String> fieldMap,
            Map<String, CellStyle> cellStyleMap,
            Map<String, CellStyle> titleStyleMap) {
        return buildSheet(objList, fieldMap, cellStyleMap, titleStyleMap, StringUtils.EMPTY_STRING);
    }

    @Override
    public WorkbookBuilder buildSheet(List<?> objList,
            Map<String, String> fieldMap,
            Map<String, CellStyle> cellStyleMap,
            Map<String, CellStyle> titleStyleMap,
            String sheetName) {

        if (this.workbook == null) {
            throw new IllegalArgumentException(MSG_WORK_BOOK_NOT_SET,
                    new NullPointerException(MSG_WORK_BOOK_IS_NULL));
        }

        // 创建空工作表
        Sheet sheet = createSheet(sheetName);

        if (objList == null) {
            return this;
        }

        Set<String> columns = fieldMap.keySet();
        Iterator<String> columnIterator = columns.iterator();
        Iterator<?> rowIterator = objList.iterator();

        // 初始化行/列游标
        AtomicInteger rowIndex = new AtomicInteger(0);
        AtomicInteger columnIndex = new AtomicInteger(0);

        // 创建标题行
        Row titleBar = sheet.createRow(rowIndex.getAndIncrement());
        while (columnIterator.hasNext()) {
            String columnName = columnIterator.next();

            // 创建单元格
            Cell cell = titleBar.createCell(columnIndex.getAndIncrement());

            // 设置title内容
            cell.setCellValue(fieldMap.containsKey(columnName) ? fieldMap.get(columnName) : StringUtils.EMPTY_STRING);

            // 设置title样式
            if (titleStyleMap != null && titleStyleMap.containsKey(columnName)) {
                cell.setCellStyle(titleStyleMap.get(columnName));
            }
        }

        while (rowIterator.hasNext()) {
            ReflectionBean rowValue = new PropertyDescriptorReflectionBean(rowIterator.next());

            // 创建正文行
            Row row = sheet.createRow(rowIndex.getAndIncrement());

            // 重置列遍历器和列游标
            columnIterator = columns.iterator();
            columnIndex.set(0);

            while (columnIterator.hasNext()) {
                String columnName = columnIterator.next();
                Cell cell = row.createCell(columnIndex.getAndIncrement());

                // 设置正文内容
                cell.setCellValue(rowValue.hasField(columnName) && rowValue.getFieldValue(columnName) != null
                        ? rowValue.getFieldValue(columnName)
                                .toString()
                        : StringUtils.EMPTY_STRING);

                // 设置正文格式
                if (cellStyleMap != null && cellStyleMap.containsKey(columnName)) {
                    cell.setCellStyle(cellStyleMap.get(columnName));
                }
            }
        }

        return this;
    }

    @Override
    public WorkbookBuilder buildSheet(List<?> objList, Map<String, String> fieldMap) throws InstantiationException {
        return buildSheet(objList, fieldMap, StringUtils.EMPTY_STRING);
    }

    @Override
    public WorkbookBuilder buildSheet(List<?> objList, Map<String, String> fieldMap, String sheetName)
            throws InstantiationException {
        Map<String, CellStyle> cellStyleMap = new HashMap<>();
        CellStyle defaultCellStyle = createCellStyle();

        // 构建默认样式映射表
        Iterator<String> columnIterator = fieldMap.keySet()
                .iterator();
        while (columnIterator.hasNext()) {
            cellStyleMap.put(columnIterator.next(), defaultCellStyle);
        }

        return buildSheet(objList, fieldMap, cellStyleMap, cellStyleMap, sheetName);
    }

    @Override
    public <T> WorkbookBuilder buildSheet(List<T> objList, Class<T> cls) throws InstantiationException {
        return buildSheet(objList, cls, StringUtils.EMPTY_STRING);
    }

    @Override
    public <T> WorkbookBuilder buildSheet(List<T> objList, Class<T> cls, String sheetName)
            throws InstantiationException {
        if (this.workbook == null) {
            throw new IllegalArgumentException(MSG_WORK_BOOK_NOT_SET,
                    new NullPointerException(MSG_WORK_BOOK_IS_NULL));
        }

        if (objList == null || objList.isEmpty()) {
            createSheet(sheetName);
            return this;
        }

        List<Field> fields = ReflectionUtils.getFields(cls)
                .stream()
                .filter(r -> r.isAnnotationPresent(FieldMap.class))
                .sorted((r, s) -> r.getAnnotation(FieldMap.class)
                        .order()
                        - s.getAnnotation(FieldMap.class)
                                .order())
                .collect(Collectors.toList());

        AtomicInteger sheetIndex = new AtomicInteger(0);

        while (sheetIndex.getAndIncrement() * MAX_ROW_COUNT < objList.size()) {
            // 创建空工作表
            Sheet sheet = createSheet(String.format("%s-%d", sheetName, sheetIndex.get()));
            // 初始化行/列游标
            AtomicInteger rowIndex = new AtomicInteger(0);
            AtomicInteger columnIndex = new AtomicInteger(0);// 创建标题行
            Row titleBar = sheet.createRow(rowIndex.getAndIncrement());
            for (Field field : fields) {
                FieldMap fieldMap = field.getAnnotation(FieldMap.class);
                String columnName = StringUtils.isEmpty(fieldMap.value()) ? field.getName() : fieldMap.value();

                // 创建单元格
                Cell cell = titleBar.createCell(columnIndex.getAndIncrement());

                // 设置title内容
                cell.setCellValue(columnName);
            }
            for (AtomicInteger index = new AtomicInteger(MAX_ROW_COUNT * (sheetIndex.get() - 1)); index.get() < Math
                    .min(MAX_ROW_COUNT * sheetIndex.get(), objList.size());) {
                T obj = objList.get(index.getAndIncrement());
                JsonNode rowValue = JacksonUtils.create()
                        .valueToTree(obj);

                // 创建正文行
                Row row = sheet.createRow(rowIndex.getAndIncrement());

                columnIndex.set(0);

                for (Field field : fields) {
                    FieldMap fieldMap = field.getAnnotation(FieldMap.class);
                    String fieldName = field.getName();
                    Cell cell = row.createCell(columnIndex.getAndIncrement());

                    // 设置正文内容
                    cell.setCellValue(
                            rowValue.hasNonNull(fieldName)
                                    ? String.join(StringUtils.EMPTY_STRING, fieldMap.prefix(), rowValue.get(fieldName)
                                            .asText(), fieldMap.suffix())
                                    : StringUtils.EMPTY_STRING);
                }
            }
        }
        return this;
    }

    @Override
    public void saveAs(String path) throws IOException {
        saveAs(new File(path));
    }

    @Override
    public void saveAs(Path path) throws IOException {
        saveAs(path.toFile());
    }

    @Override
    public void saveAs(File file) throws IOException {

        if (file.exists()) {
            Files.delete(file.toPath());
        }

        OutputStream out = new FileOutputStream(file);
        try {
            this.workbook.write(out);
        } finally {
            out.close();
        }
    }

    @Override
    public void saveAs(OutputStream os) throws IOException {
        this.workbook.write(os);
    }

    /**
     * 
     * 设置工作簿类型
     * 
     * @param workbookType
     *            工作簿类型
     * @since JDK 1.8
     */
    private void initWorkbook(WorkbookType workbookType) {
        switch (workbookType) {
            case XLS:
                this.workbook = new HSSFWorkbook();
                break;
            case XLSX:
                this.workbook = new XSSFWorkbook();
                break;
            default:
                this.workbook = null;
                break;
        }
    }

    /**
     * 
     * 获取工作簿实例
     * 
     * @param pathStr
     *            工作簿路径
     * @return 工作簿实例
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    private Workbook loadWorkbook(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        File file = path.toFile();

        if (path.endsWith(WorkbookType.XLS.info())) {
            this.workbook = new HSSFWorkbook(new FileInputStream(file));
        } else {
            this.workbook = new XSSFWorkbook(new FileInputStream(file));
        }

        return this.workbook;
    }

    /**
     * 
     * 获取工作簿实例
     * 
     * @param is
     *            输入流
     * @param workbookType
     *            工作簿类型
     * @return 工作簿实例
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    private Workbook loadWorkbook(InputStream is, WorkbookType workbookType) throws IOException {
        if (workbookType == WorkbookType.XLS) {
            this.workbook = new HSSFWorkbook(is);
        } else {
            this.workbook = new XSSFWorkbook(is);
        }

        return this.workbook;
    }

    /**
     * 
     * 创建空的工作表实例
     * 
     * @param sheetName
     *            工作表名称
     * @return 空的工作表实例
     * @since JDK 1.8
     */
    private Sheet createSheet(String sheetName) {
        if (this.workbook == null) {
            throw new IllegalArgumentException(MSG_WORK_BOOK_NOT_SET,
                    new NullPointerException(MSG_WORK_BOOK_IS_NULL));
        }

        if (StringUtils.isEmpty(sheetName)) {
            return this.workbook.createSheet();
        }

        return this.workbook.createSheet(sheetName);
    }

    /**
     * 
     * 获取单元格样式实例
     * 
     * @return 单元格样式实例
     * @since JDK 1.8
     */
    private CellStyle createCellStyle() {
        if (this.workbook == null) {
            throw new IllegalArgumentException(MSG_WORK_BOOK_NOT_SET,
                    new NullPointerException(MSG_WORK_BOOK_IS_NULL));
        }

        return this.workbook.createCellStyle();
    }

    /**
     * 
     * 获取字体实例
     * 
     * @return 字体实例
     * @since JDK 1.8
     */
    public Font createFont() {
        if (this.workbook == null) {
            throw new IllegalArgumentException(MSG_WORK_BOOK_NOT_SET,
                    new NullPointerException(MSG_WORK_BOOK_IS_NULL));
        }

        return this.workbook.createFont();
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    @Override
    public <T> List<T> mapList(int columnIndex, Class<T> type) {
        return mapList(columnIndex, 0, type);
    }

    @Override
    public <T> List<T> mapList(int columnIndex, int offsetRow, Class<T> type) {
        if (this.workbook == null) {
            throw new IllegalArgumentException(MSG_WORK_BOOK_NOT_SET,
                    new NullPointerException(MSG_WORK_BOOK_IS_NULL));
        }

        if (workbook.getNumberOfSheets() <= 0) {
            throw new WorkbookException("Can not find any sheet in this workbook.");
        }

        List<T> list = new ArrayList<>();

        Sheet sheet = workbook.getSheetAt(0);
        if (sheet.getLastRowNum() < 0) {
            return list;
        }

        AtomicInteger rowCount = new AtomicInteger(offsetRow);
        while (rowCount.get() <= sheet.getLastRowNum()) {
            Row row = sheet.getRow(rowCount.getAndIncrement());
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(columnIndex);
            if (cell == null) {
                continue;
            }
            cell.setCellType(CellType.STRING);
            String stringValue = cell.getStringCellValue();
            if (StringUtils.isEmpty(stringValue)) {
                continue;
            }

            list.add(TypeUtils.fromString(stringValue, type));
        }

        return list;
    }
}
