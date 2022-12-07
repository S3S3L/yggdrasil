package io.github.s3s3l.yggdrasil.fsm;

import java.util.Set;

import io.github.s3s3l.yggdrasil.fsm.input.Input;
import io.github.s3s3l.yggdrasil.fsm.output.Output;
import io.github.s3s3l.yggdrasil.fsm.state.State;
import io.github.s3s3l.yggdrasil.fsm.state.Stateful;
import io.github.s3s3l.yggdrasil.fsm.transition.Transition;

/**
 * <p>
 * </p>
 * Date: Sep 9, 2019 5:12:19 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface FiniteStateMachine extends Stateful {

    /**
     * 
     * 获取所有可用的状态
     * 
     * @return 所有可用状态的集合
     * @since JDK 1.8
     */
    Set<State> getStateSet();

    /**
     * 
     * 根据输入值执行状态转换
     * 
     * @param input 输入
     * @return 状态转换结果
     * @since JDK 1.8
     */
    Output doTransition(Input input);

    /**
     * 
     * 添加状态转换器
     * 
     * @param state 状态
     * @param transition 转换器
     * @param override 如果该状态的转换器已存在，是否覆盖
     * @since JDK 1.8
     */
    void addTransition(State state, Transition transition, boolean override);
    
    /**
     * 
     * 移除状态转换器
     * 
     * @param state 
     * @since JDK 1.8
     */
    void removeTransition(State state);
}
