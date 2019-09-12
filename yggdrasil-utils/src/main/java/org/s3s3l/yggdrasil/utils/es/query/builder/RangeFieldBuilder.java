package org.s3s3l.yggdrasil.utils.es.query.builder;

/**
 * <p>
 * </p>
 * ClassName:RangeFieldBuilder <br>
 * Date: Jan 3, 2019 1:22:07 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface RangeFieldBuilder<T> {

    /**
     * 
     * 大于或等于
     * 
     * @param gte
     *            比较值
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    public RangeFieldBuilder<T> gte(String gte);

    /**
     * 
     * 大于
     * 
     * @param gt
     *            比较值
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    public RangeFieldBuilder<T> gt(String gt);

    /**
     * 
     * 小于或等于
     * 
     * @param lte
     *            比较值
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    public RangeFieldBuilder<T> lte(String lte);

    /**
     * 
     * 小于
     * 
     * @param lt
     *            比较值
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    public RangeFieldBuilder<T> lt(String lt);

    /**
     * 
     * 时间格式
     * 
     * @param format
     *            时间格式
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    public RangeFieldBuilder<T> format(String format);

    /**
     * 
     * 时区
     * 
     * @param timeZone
     *            时区
     * @return {@link RangeFieldBuilder}
     * @since JDK 1.8
     */
    public RangeFieldBuilder<T> timeZone(String timeZone);

    /**
     * 
     * 字段构建完成
     * 
     * @return {@link RangeFieldCreator}
     * @since JDK 1.8
     */
    public RangeFieldCreator<T> fieldDone();

}
