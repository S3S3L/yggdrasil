package io.github.s3s3l.yggdrasil.fsm;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import io.github.s3s3l.yggdrasil.fsm.exception.TransitionException;
import io.github.s3s3l.yggdrasil.fsm.input.Input;
import io.github.s3s3l.yggdrasil.fsm.output.Output;
import io.github.s3s3l.yggdrasil.fsm.state.State;
import io.github.s3s3l.yggdrasil.fsm.transition.Transition;
import io.github.s3s3l.yggdrasil.persistence.Versioning;
import io.github.s3s3l.yggdrasil.utils.verify.Verify;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * </p>
 * Date: Sep 10, 2019 2:50:00 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@RequiredArgsConstructor
public abstract class AbstractFsm implements FiniteStateMachine, Versioning {

    @Getter
    protected final String id;
    @Getter
    protected final String version;
    @NonNull
    @Getter
    protected State state;
    @Getter
    protected final ConcurrentMap<State, Transition> transitions;


    @Override
    public Set<State> getStateSet() {
        return transitions.keySet();
    }

    @Override
    public Output doTransition(Input input) {
        Verify.notNull(input);
        Output o;
        while ((o = transitions.get(state)
                .fire(input)).isContinueTrans()) {
            state = o.getState();
            if (state == null) {
                throw new TransitionException("Output state can not be NULL");
            }
        }

        return o;
    }

    @Override
    public void addTransition(State state, Transition transition, boolean override) {
        Verify.notNull(state);
        Verify.notNull(transition);
        if (override) {
            transitions.put(state, transition);
        } else {
            transitions.putIfAbsent(state, transition);
        }
    }

    @Override
    public void removeTransition(State state) {
        Verify.notNull(state);
        transitions.remove(state);
    }

}
