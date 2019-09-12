package org.s3s3l.yggdrasil.utils.es.query.builder;

import org.s3s3l.yggdrasil.utils.es.enumerations.OrderType;
import org.s3s3l.yggdrasil.utils.es.enumerations.SortMode;

/**
 * <p>
 * </p>
 * ClassName:SortPropertyBuilder <br>
 * Date: Jan 7, 2019 12:10:08 AM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface SortPropertyBuilder {

    /**
     * 
     * 选择排序类型
     * 
     * @param orderType
     *            排序类型
     * @return {@link SortPropertyBuilder}
     * @since JDK 1.8
     * @see OrderType
     */
    SortPropertyBuilder order(OrderType orderType);

    /**
     * 
     * 选择排序模式
     * 
     * @param sortMode
     *            排序模式
     * @return {@link SortPropertyBuilder}
     * @since JDK 1.8
     * @see sortMode
     */
    SortPropertyBuilder mode(SortMode sortMode);

    /**
     * 
     * 设置缺省值
     * 
     * @param missing
     *            缺省值 {@code _last} or {@code _first} or custom value
     * @return {@link SortPropertyBuilder}
     * @since JDK 1.8
     */
    SortPropertyBuilder missing(String missing);

    /**
     * 
     * 排序属性设置完成
     * 
     * @return {@link SortSelector}
     * @since JDK 1.8
     */
    SortSelector propertyDone();
}
