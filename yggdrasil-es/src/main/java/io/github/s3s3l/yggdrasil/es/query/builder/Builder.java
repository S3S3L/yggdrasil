package io.github.s3s3l.yggdrasil.es.query.builder;

import io.github.s3s3l.yggdrasil.es.query.Esdsl;

/**
 * <p>
 * </p> 
 * ClassName:Builder <br> 
 * Date:     Jan 7, 2019 11:18:05 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface Builder {
    /**
     * 
     * 构建DSL
     * 
     * @return {@link Esdsl}
     * @since JDK 1.8
     */
    Esdsl build();
}
  