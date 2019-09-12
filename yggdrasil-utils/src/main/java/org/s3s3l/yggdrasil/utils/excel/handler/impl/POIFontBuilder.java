package org.s3s3l.yggdrasil.utils.excel.handler.impl;

import org.apache.poi.ss.usermodel.Font;
import org.s3s3l.yggdrasil.utils.excel.handler.FontBuilder;

/**
 * <p>
 * </p>
 * ClassName:POIFontBuilder <br>
 * Date: Nov 14, 2016 5:23:59 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class POIFontBuilder implements FontBuilder {

    private Font font;

    private void setFont(Font font) {
        this.font = font;
    }

    private POIFontBuilder() {
    }

    /**
     * 
     * 获取实例
     * 
     * @param font
     *            字体对象
     * @return 新构造器实例
     * @since JDK 1.8
     */
    public static POIFontBuilder create(Font font) {
        POIFontBuilder tmp = new POIFontBuilder();
        tmp.setFont(font);

        return tmp;
    }

    @Override
    public Font build() {
        return font;
    }

    @Override
    public FontBuilder setFontName(String name) {
        font.setFontName(name);
        return this;
    }

    @Override
    public FontBuilder setFontHeight(short height) {
        font.setFontHeight(height);
        return this;
    }

    @Override
    public FontBuilder setFontHeightInPoints(short height) {
        font.setFontHeightInPoints(height);
        return this;
    }

    @Override
    public FontBuilder setItalic(boolean italic) {
        font.setItalic(italic);
        return this;
    }

    @Override
    public FontBuilder setStrikeout(boolean strikeout) {
        font.setStrikeout(strikeout);
        return this;
    }

    @Override
    public FontBuilder setColor(short color) {
        font.setColor(color);
        return this;
    }

    @Override
    public FontBuilder setTypeOffset(short offset) {
        font.setTypeOffset(offset);
        return this;
    }

    @Override
    public FontBuilder setUnderline(byte underline) {
        font.setUnderline(underline);
        return this;
    }

    @Override
    public FontBuilder setCharSet(byte charset) {
        font.setCharSet(charset);
        return this;
    }

    @Override
    public FontBuilder setCharSet(int charset) {
        font.setCharSet(charset);
        return this;
    }

    /**
     * @deprecated
     * @see org.s3s3l.yggdrasil.utils.excel.handler.FontBuilder#setBoldweight(short)
     */
    @Override
    @Deprecated
    public FontBuilder setBoldweight(short boldweight) {
        font.setBoldweight(boldweight);
        return this;
    }

    @Override
    public FontBuilder setBold(boolean bold) {
        font.setBold(bold);
        return this;
    }

}
