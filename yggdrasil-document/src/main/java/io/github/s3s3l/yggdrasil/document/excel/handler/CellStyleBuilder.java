package io.github.s3s3l.yggdrasil.document.excel.handler;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * <p>
 * </p>
 * ClassName:CellStyleBuilder <br>
 * Date: Nov 14, 2016 5:22:34 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface CellStyleBuilder {

    /**
     * 
     * 获取构建好的<code>CellStyle</code>对象
     * 
     * @return 单元格风格对象
     * @since JDK 1.8
     */
    CellStyle build();

    /**
     * 
     * set the data format (must be a valid format)
     * 
     * @param fmt
     *            format
     * @return builder
     * @since JDK 1.8
     * @see DataFormat
     */
    CellStyleBuilder setDataFormat(short fmt);

    /**
     * 
     * set the font for this style
     * 
     * @param font
     *            a font object created or retreived from the Workbook object
     * @return builder
     * @since JDK 1.8
     * @see Workbook#createFont()
     * @see Workbook#getFontAt(short)
     */
    CellStyleBuilder setFont(Font font);

    /**
     * 
     * set the cell's using this style to be hidden
     * 
     * @param hidden
     *            - whether the cell using this style should be hidden
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setHidden(boolean hidden);

    /**
     * 
     * set the cell's using this style to be locked
     * 
     * @param locked
     *            - whether the cell using this style should be locked
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setLocked(boolean locked);

    /**
     * 
     * set the type of horizontal alignment for the cell <br>
     * #ALIGN_GENERAL <br>
     * #ALIGN_LEFT <br>
     * #ALIGN_CENTER <br>
     * #ALIGN_RIGHT <br>
     * #ALIGN_FILL <br>
     * #ALIGN_JUSTIFY <br>
     * #ALIGN_CENTER_SELECTION
     * 
     * @param align
     *            - the type of alignment
     * @return builder
     * @deprecated
     * @since JDK 1.8
     */
    @Deprecated
    CellStyleBuilder setAlignment(HorizontalAlignment align);

    /**
     * 
     * Set whether the text should be wrapped. Setting this flag to
     * <code>true</code> make all content visible whithin a cell by displaying
     * it on multiple lines
     * 
     * @param wrapped
     *            wrap text or not
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setWrapText(boolean wrapped);

    /**
     * 
     * set the type of vertical alignment for the cell <br>
     * #VERTICAL_TOP <br>
     * #VERTICAL_CENTER <br>
     * #VERTICAL_BOTTOM <br>
     * #VERTICAL_JUSTIFY
     * 
     * @param align
     *            the type of alignment
     * @return builder
     * @deprecated
     * @since JDK 1.8
     */
    @Deprecated
    CellStyleBuilder setVerticalAlignment(VerticalAlignment align);

    /**
     * 
     * set the degree of rotation for the text in the cell
     * 
     * @param rotation
     *            degrees (between -90 and 90 degrees)
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setRotation(short rotation);

    /**
     * 
     * set the number of spaces to indent the text in the cell
     * 
     * @param indent
     *            - number of spaces
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setIndention(short indent);

    /**
     * 
     * set the type of border to use for the left border of the cell <br>
     * #BORDER_NONE <br>
     * #BORDER_THIN <br>
     * #BORDER_MEDIUM <br>
     * #BORDER_DASHED <br>
     * #BORDER_DOTTED <br>
     * #BORDER_THICK <br>
     * #BORDER_DOUBLE <br>
     * #BORDER_HAIR <br>
     * #BORDER_MEDIUM_DASHED <br>
     * #BORDER_DASH_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT <br>
     * #BORDER_DASH_DOT_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT_DOT <br>
     * #BORDER_SLANTED_DASH_DOT
     * 
     * @param border
     *            type
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setBorderLeft(BorderStyle border);

    /**
     * 
     * set the type of border to use for the right border of the cell <br>
     * #BORDER_NONE <br>
     * #BORDER_THIN <br>
     * #BORDER_MEDIUM <br>
     * #BORDER_DASHED <br>
     * #BORDER_DOTTED <br>
     * #BORDER_THICK <br>
     * #BORDER_DOUBLE <br>
     * #BORDER_HAIR <br>
     * #BORDER_MEDIUM_DASHED <br>
     * #BORDER_DASH_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT <br>
     * #BORDER_DASH_DOT_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT_DOT <br>
     * #BORDER_SLANTED_DASH_DOT
     * 
     * @param border
     *            type
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setBorderRight(BorderStyle border);

    /**
     * 
     * set the type of border to use for the top border of the cell <br>
     * #BORDER_NONE <br>
     * #BORDER_THIN <br>
     * #BORDER_MEDIUM <br>
     * #BORDER_DASHED <br>
     * #BORDER_DOTTED <br>
     * #BORDER_THICK <br>
     * #BORDER_DOUBLE <br>
     * #BORDER_HAIR <br>
     * #BORDER_MEDIUM_DASHED <br>
     * #BORDER_DASH_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT <br>
     * #BORDER_DASH_DOT_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT_DOT <br>
     * #BORDER_SLANTED_DASH_DOT
     * 
     * @param border
     *            type
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setBorderTop(BorderStyle border);

    /**
     * 
     * set the type of border to use for the bottom border of the cell <br>
     * #BORDER_NONE <br>
     * #BORDER_THIN <br>
     * #BORDER_MEDIUM <br>
     * #BORDER_DASHED <br>
     * #BORDER_DOTTED <br>
     * #BORDER_THICK <br>
     * #BORDER_DOUBLE <br>
     * #BORDER_HAIR <br>
     * #BORDER_MEDIUM_DASHED <br>
     * #BORDER_DASH_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT <br>
     * #BORDER_DASH_DOT_DOT <br>
     * #BORDER_MEDIUM_DASH_DOT_DOT <br>
     * #BORDER_SLANTED_DASH_DOT
     * 
     * @param border
     *            type
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setBorderBottom(BorderStyle border);

    /**
     * 
     * set the color to use for the left border
     * 
     * @param color
     *            The index of the color definition
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setLeftBorderColor(short color);

    /**
     * 
     * set the color to use for the right border
     * 
     * @param color
     *            The index of the color definition
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setRightBorderColor(short color);

    /**
     * 
     * set the color to use for the top border
     * 
     * @param color
     *            The index of the color definition
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setTopBorderColor(short color);

    /**
     * 
     * set the color to use for the bottom border
     * 
     * @param color
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setBottomBorderColor(short color);

    /**
     * 
     * setting to one fills the cell with the foreground color... No idea about
     * other values <br>
     * #NO_FILL <br>
     * #SOLID_FOREGROUND <br>
     * #FINE_DOTS <br>
     * #ALT_BARS <br>
     * #SPARSE_DOTS <br>
     * #THICK_HORZ_BANDS <br>
     * #THICK_VERT_BANDS <br>
     * #THICK_BACKWARD_DIAG <br>
     * #THICK_FORWARD_DIAG <br>
     * #BIG_SPOTS <br>
     * #BRICKS <br>
     * #THIN_HORZ_BANDS <br>
     * #THIN_VERT_BANDS <br>
     * #THIN_BACKWARD_DIAG <br>
     * #THIN_FORWARD_DIAG <br>
     * #SQUARES <br>
     * #DIAMONDS
     * 
     * @param fp
     *            fill pattern (set to 1 to fill w/foreground color)
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setFillPattern(FillPatternType fp);

    /**
     * 
     * set the background fill color.
     * 
     * @param bg
     *            color
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setFillBackgroundColor(short bg);

    /**
     * 
     * set the foreground fill color <i>Note: Ensure Foreground color is set
     * prior to background color.</i>
     * 
     * @param bg
     *            color
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setFillForegroundColor(short bg);

    /**
     * 
     * Clones all the style information from another CellStyle, onto this one.
     * This CellStyle will then have all the same properties as the source, but
     * the two may be edited independently. Any stylings on this CellStyle will
     * be lost!
     * 
     * The source CellStyle could be from another Workbook if you like. This
     * allows you to copy styles from one Workbook to another.
     *
     * However, both of the CellStyles will need to be of the same type
     * (HSSFCellStyle or XSSFCellStyle)
     * 
     * @param source
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder cloneStyleFrom(CellStyle source);

    /**
     * 
     * Controls if the Cell should be auto-sized to shrink to fit if the text is
     * too long
     * 
     * @param shrinkToFit
     * @return builder
     * @since JDK 1.8
     */
    CellStyleBuilder setShrinkToFit(boolean shrinkToFit);

}
