package org.s3s3l.yggdrasil.utils.es.query;

/**
 * <p>
 * </p>
 * ClassName:RangableQueryBlock <br>
 * Date: Jan 3, 2019 1:04:15 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface RangableQueryBlock extends QueryBlock {

    /**
     * 
     * 设置区间查询条件
     * 
     * @param range
     *            区间查询条件
     * @since JDK 1.8
     */
    void setRange(Range range);
}
