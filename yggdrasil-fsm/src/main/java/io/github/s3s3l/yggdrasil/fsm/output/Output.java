package io.github.s3s3l.yggdrasil.fsm.output;

import io.github.s3s3l.yggdrasil.fsm.state.Stateful;

/**
 * <p>
 * </p> 
 * Date:     Sep 10, 2019 5:05:38 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public interface Output extends Stateful {

    default boolean isContinueTrans() {
        return false;
    }
}
  