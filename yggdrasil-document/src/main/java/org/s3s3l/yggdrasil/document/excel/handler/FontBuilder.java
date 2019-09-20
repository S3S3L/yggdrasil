package org.s3s3l.yggdrasil.document.excel.handler;

import org.apache.poi.ss.usermodel.Font;

/**
 * <p>
 * </p>
 * ClassName:FontBuilder <br>
 * Date: Nov 14, 2016 5:22:06 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface FontBuilder {

    /**
     * 
     * 获取构建好的<code>Font</code>对象
     * 
     * @author kehw_zwei
     * @return 字体对象
     * @since JDK 1.8
     */
    Font build();

    /**
     * 
     * set the name for the font (i.e. Arial)
     * 
     * @param name
     *            String representing the name of the font to use
     * @since JDK 1.8
     */
    FontBuilder setFontName(String name);

    /**
     * 
     * set the font height in unit's of 1/20th of a point. Maybe you might want
     * to use the setFontHeightInPoints which matches to the familiar 10, 12, 14
     * etc..
     * 
     * @param height
     *            height in 1/20ths of a point
     * @since JDK 1.8
     * @see #setFontHeightInPoints(short)
     */
    FontBuilder setFontHeight(short height);

    /**
     * 
     * set the font height
     * 
     * @param height
     *            height in the familiar unit of measure - points
     * @since JDK 1.8
     * @see #setFontHeight(short)
     */
    FontBuilder setFontHeightInPoints(short height);

    /**
     * 
     * set whether to use italics or not
     * 
     * @param italic
     *            italics or not
     * @since JDK 1.8
     */
    FontBuilder setItalic(boolean italic);

    /**
     * 
     * set whether to use a strikeout horizontal line through the text or not
     * 
     * @param strikeout
     *            strikeout or not
     * @since JDK 1.8
     */
    FontBuilder setStrikeout(boolean strikeout);

    /**
     * 
     * set the color for the font <br>
     * COLOR_NORMAL Note: Use this rather than HSSFColor.AUTOMATIC for default
     * font color <br>
     * COLOR_RED
     * 
     * @param color
     *            color to use
     * @since JDK 1.8
     */
    FontBuilder setColor(short color);

    /**
     * 
     * set normal,super or subscript. <br>
     * SS_NONE <br>
     * SS_SUPER <br>
     * SS_SUB
     * 
     * @param offset
     *            offset type to use (none,super,sub)
     * @since JDK 1.8
     */
    FontBuilder setTypeOffset(short offset);

    /**
     * 
     * set type of text underlining to use <br>
     * U_NONE <br>
     * U_SINGLE <br>
     * U_DOUBLE <br>
     * U_SINGLE_ACCOUNTING <br>
     * U_DOUBLE_ACCOUNTING
     * 
     * @param underline
     * @since JDK 1.8
     */
    FontBuilder setUnderline(byte underline);

    /**
     * 
     * set character-set to use. <br>
     * ANSI_CHARSET <br>
     * DEFAULT_CHARSET <br>
     * SYMBOL_CHARSET
     * 
     * @param charset
     * @since JDK 1.8
     */
    FontBuilder setCharSet(byte charset);

    /**
     * 
     * set character-set to use. <br>
     * ANSI_CHARSET <br>
     * DEFAULT_CHARSET <br>
     * SYMBOL_CHARSET
     * 
     * @param charset
     * @since JDK 1.8
     */
    FontBuilder setCharSet(int charset);

    /**
     * 
     * 设置线粗
     * 
     * @param boldweight
     * @since JDK 1.8
     */
    FontBuilder setBoldweight(short boldweight);

    /**
     * 
     * 设置是否加粗
     * 
     * @param bold
     * @since JDK 1.8
     */
    FontBuilder setBold(boolean bold);

}
