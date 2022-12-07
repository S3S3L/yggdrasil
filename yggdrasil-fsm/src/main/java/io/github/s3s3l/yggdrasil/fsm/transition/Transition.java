package io.github.s3s3l.yggdrasil.fsm.transition;

import java.util.Set;

import io.github.s3s3l.yggdrasil.fsm.input.Input;
import io.github.s3s3l.yggdrasil.fsm.output.Output;
import io.github.s3s3l.yggdrasil.fsm.state.State;
import io.github.s3s3l.yggdrasil.fsm.state.Stateful;

/**
 * <p>
 * </p>
 * Date: Sep 9, 2019 4:59:36 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Transition extends Stateful {

    /**
     * 
     * 执行状态转换
     * 
     * @param input
     *            输入
     * @return 状态转化结果，包含后续状态
     * @since JDK 1.8
     */
    Output fire(Input input);
    
    /**
     * 
     * 所有可能的后续状态集合
     * 
     * @return 所有可能的后续状态集合
     * @since JDK 1.8
     */
    Set<State> getOutStateSet();
}
