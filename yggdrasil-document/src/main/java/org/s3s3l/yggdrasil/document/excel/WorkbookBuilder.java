package org.s3s3l.yggdrasil.document.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.s3s3l.yggdrasil.document.ResourceMapper;
import org.s3s3l.yggdrasil.document.excel.handler.CellStyleBuilder;
import org.s3s3l.yggdrasil.document.excel.handler.FontBuilder;

/**
 * <p>
 * </p>
 * ClassName:WorkbookBuilder <br>
 * Date: Nov 14, 2016 5:18:58 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface WorkbookBuilder extends ResourceMapper {

    Workbook getWorkbook();

    FontBuilder getFontBuilder();

    CellStyleBuilder getCellStyleBuilder();

    /**
     * 
     * 构建工作表
     * 
     * @param objList
     *            数据列表
     * @param fieldMap
     *            字段映射表
     * @param cellStyleMap
     *            正文样式映射表
     * @param titleStyleMap
     *            标题样式映射表
     * @return 按指定样式和数据构建的工作表
     * @since JDK 1.8
     */
    public WorkbookBuilder buildSheet(List<?> objList,
            Map<String, String> fieldMap,
            Map<String, CellStyle> cellStyleMap,
            Map<String, CellStyle> titleStyleMap);

    /**
     * 
     * 构建工作表
     * 
     * @param objList
     *            数据列表
     * @param fieldMap
     *            字段映射表
     * @param cellStyleMap
     *            正文样式映射表
     * @param titleStyleMap
     *            标题样式映射表
     * @param sheetName
     *            工作表名称
     * @return 按指定样式和数据构建的工作表
     * @since JDK 1.8
     */
    public WorkbookBuilder buildSheet(List<?> objList,
            Map<String, String> fieldMap,
            Map<String, CellStyle> cellStyleMap,
            Map<String, CellStyle> titleStyleMap,
            String sheetName);

    /**
     * 
     * 构建工作表
     * 
     * @param objList
     *            数据列表
     * @param fieldMap
     *            字段映射表
     * @return 按给定数据和默认样式构建的工作表
     * @throws InstantiationException
     *             {@link InstantiationException}
     * @since JDK 1.8
     */
    public WorkbookBuilder buildSheet(List<?> objList, Map<String, String> fieldMap) throws InstantiationException;

    /**
     * 
     * 构建工作表
     * 
     * @param objList
     *            数据列表
     * @param fieldMap
     *            字段映射表
     * @param sheetName
     *            工作表名称
     * @return 按给定数据和默认样式构建的工作表
     * @throws InstantiationException
     *             {@link InstantiationException}
     * @since JDK 1.8
     */
    public WorkbookBuilder buildSheet(List<?> objList, Map<String, String> fieldMap, String sheetName)
            throws InstantiationException;

    /**
     * 
     * 构建工作表
     * 
     * @param objList
     *            数据列表
     * @return 按给定数据构建的工作表
     * @throws InstantiationException
     *             {@link InstantiationException}
     * @since JDK 1.8
     */
    public <T> WorkbookBuilder buildSheet(List<T> objList, Class<T> cls) throws InstantiationException;

    /**
     * 
     * 构建工作表
     * 
     * @param objList
     *            数据列表
     * @param sheetName
     *            工作表名称
     * @return 按给定数据构建的工作表
     * @throws InstantiationException
     *             {@link InstantiationException}
     * @since JDK 1.8
     */
    public <T> WorkbookBuilder buildSheet(List<T> objList, Class<T> cls, String sheetName)
            throws InstantiationException;

    /**
     * 
     * 保存工作簿（会覆盖原文件）
     * 
     * @param path
     *            路径
     * @throws FileNotFoundException
     *             {@link FileNotFoundException}
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public void saveAs(String path) throws IOException;

    /**
     * 
     * 保存工作簿（会覆盖原文件）
     * 
     * @param path
     *            路径
     * @throws FileNotFoundException
     *             {@link FileNotFoundException}
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public void saveAs(Path path) throws IOException;

    /**
     * 
     * 保存工作簿（会覆盖原文件）
     * 
     * @param file
     *            文件
     * @throws FileNotFoundException
     *             {@link FileNotFoundException}
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public void saveAs(File file) throws IOException;

    /**
     * 
     * 保存工作簿到输出流
     * 
     * @param os
     *            输出流
     * @throws IOException
     *             {@link IOException}
     * @since JDK 1.8
     */
    public void saveAs(OutputStream os) throws IOException;

}
