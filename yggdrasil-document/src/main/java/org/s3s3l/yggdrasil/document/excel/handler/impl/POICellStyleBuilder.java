package org.s3s3l.yggdrasil.document.excel.handler.impl;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.s3s3l.yggdrasil.document.excel.handler.CellStyleBuilder;

/**
 * <p>
 * </p>
 * ClassName:POICellStyleBuilder <br>
 * Date: Nov 14, 2016 5:24:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class POICellStyleBuilder implements CellStyleBuilder {

    private CellStyle cellStyle;

    private void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }

    private POICellStyleBuilder() {
    }

    /**
     * 
     * 获取实例
     * 
     * @param cellStyle
     *            单元格风格对象
     * @return 新构造器实例
     * @since JDK 1.8
     */
    public static CellStyleBuilder create(CellStyle cellStyle) {
        POICellStyleBuilder temp = new POICellStyleBuilder();
        temp.setCellStyle(cellStyle);

        return temp;
    }

    @Override
    public CellStyle build() {
        return cellStyle;
    }

    @Override
    public CellStyleBuilder setDataFormat(short fmt) {
        cellStyle.setDataFormat(fmt);
        return this;
    }

    @Override
    public CellStyleBuilder setFont(Font font) {
        cellStyle.setFont(font);
        return this;
    }

    @Override
    public CellStyleBuilder setHidden(boolean hidden) {
        cellStyle.setHidden(hidden);
        return this;
    }

    @Override
    public CellStyleBuilder setLocked(boolean locked) {
        cellStyle.setLocked(locked);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setAlignment(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setAlignment(HorizontalAlignment align) {
        cellStyle.setAlignment(align);
        return this;
    }

    @Override
    public CellStyleBuilder setWrapText(boolean wrapped) {
        cellStyle.setWrapText(wrapped);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setVerticalAlignment(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setVerticalAlignment(VerticalAlignment align) {
        cellStyle.setVerticalAlignment(align);
        return this;
    }

    @Override
    public CellStyleBuilder setRotation(short rotation) {
        cellStyle.setRotation(rotation);
        return this;
    }

    @Override
    public CellStyleBuilder setIndention(short indent) {
        cellStyle.setIndention(indent);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setBorderLeft(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setBorderLeft(BorderStyle border) {
        cellStyle.setBorderLeft(border);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setBorderRight(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setBorderRight(BorderStyle border) {
        cellStyle.setBorderRight(border);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setBorderTop(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setBorderTop(BorderStyle border) {
        cellStyle.setBorderTop(border);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setBorderBottom(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setBorderBottom(BorderStyle border) {
        cellStyle.setBorderBottom(border);
        return this;
    }

    @Override
    public CellStyleBuilder setLeftBorderColor(short color) {
        cellStyle.setLeftBorderColor(color);
        return this;
    }

    @Override
    public CellStyleBuilder setRightBorderColor(short color) {
        cellStyle.setRightBorderColor(color);
        return this;
    }

    @Override
    public CellStyleBuilder setTopBorderColor(short color) {
        cellStyle.setTopBorderColor(color);
        return this;
    }

    @Override
    public CellStyleBuilder setBottomBorderColor(short color) {
        cellStyle.setBottomBorderColor(color);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.CellStyleBuilder#setFillPattern(short)
     */
    @Override
    @Deprecated
    public CellStyleBuilder setFillPattern(FillPatternType fp) {
        cellStyle.setFillPattern(fp);
        return this;
    }

    @Override
    public CellStyleBuilder setFillBackgroundColor(short bg) {
        cellStyle.setFillBackgroundColor(bg);
        return this;
    }

    @Override
    public CellStyleBuilder setFillForegroundColor(short bg) {
        cellStyle.setFillForegroundColor(bg);
        return this;
    }

    @Override
    public CellStyleBuilder cloneStyleFrom(CellStyle source) {
        cellStyle.cloneStyleFrom(source);
        return this;
    }

    @Override
    public CellStyleBuilder setShrinkToFit(boolean shrinkToFit) {
        cellStyle.setShrinkToFit(shrinkToFit);
        return this;
    }
}
