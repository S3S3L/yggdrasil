package org.s3s3l.yggdrasil.fsm.state;

/**
 * <p>
 * </p> 
 * Date:     Sep 10, 2019 2:12:24 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface Stateful {

    /**
     * 
     * 获取状态
     * 
     * @return 状态 
     * @since JDK 1.8
     */
    State getState();
}
  