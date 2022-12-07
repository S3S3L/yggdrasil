package io.github.s3s3l.yggdrasil.fsm;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.github.s3s3l.yggdrasil.fsm.state.State;
import io.github.s3s3l.yggdrasil.fsm.transition.Transition;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 3:41:51 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class BasicStorableFsm extends AbstractFsm {

    public BasicStorableFsm(String id, String version, State state, ConcurrentMap<State, Transition> transitions) {
        super(id, version, state, transitions);
    }

    public static BasicStorableFsmBuilder builder() {
        return new BasicStorableFsmBuilder();
    }

    public static class BasicStorableFsmBuilder {
        private String id;
        private String version;
        private State state;
        private List<Transition> transitions;

        private BasicStorableFsmBuilder() {

        }

        public BasicStorableFsmBuilder id(String id) {
            this.id = id;
            return this;
        }

        public BasicStorableFsmBuilder version(String version) {
            this.version = version;
            return this;
        }

        public BasicStorableFsmBuilder state(State state) {
            this.state = state;
            return this;
        }

        public BasicStorableFsmBuilder transitions(List<Transition> transitions) {
            this.transitions = transitions;
            return this;
        }

        public BasicStorableFsm build() {
            return new BasicStorableFsm(id, version, state, transitions.stream()
                    .collect(Collectors.toMap(Transition::getState, Function.identity(), (a, b) -> a,
                            ConcurrentHashMap::new)));
        }
    }
}
