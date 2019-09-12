package org.s3s3l.yggdrasil.utils.es.query.builder;  
/**
 * <p>
 * </p> 
 * ClassName:SortBuilder <br> 
 * Date:     Jan 7, 2019 12:05:41 AM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface SortSelector {
    /**
     * 
     * 选择排序字段
     * 
     * @param field 字段名
     * @return {@link SortPropertyBuilder}
     * @since JDK 1.8
     */
    SortPropertyBuilder field(String field);
    
    /**
     * 
     * 完成排序配置
     * 
     * @return {@link EsdslBuilder}
     * @since JDK 1.8
     */
    EsdslBuilder sortDone();
}
  