package io.github.s3s3l.yggdrasil.es.query.builder;

/**
 * <p>
 * </p> 
 * ClassName:BaseQueryBuilder <br> 
 * Date:     Jan 2, 2019 1:44:41 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface BaseQueryBuilder {

    /**
     * 
     * 查询构建完成
     * 
     * @return {@link EsdslBuilder}
     * @since JDK 1.8
     */
    EsdslBuilder queryDone();
}
  