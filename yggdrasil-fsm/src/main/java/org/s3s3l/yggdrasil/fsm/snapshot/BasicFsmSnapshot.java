package org.s3s3l.yggdrasil.fsm.snapshot;

import java.util.List;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.fsm.BasicStorableFsm;

import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 4:42:50 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Builder
@AllArgsConstructor
public class BasicFsmSnapshot extends AbstractFsmSnapshot<BasicStorableFsm> {
    private List<AbstractTransitionSnapshot> transitions;
    private AbstractStateSnapshot state;

    @Override
    public BasicStorableFsm restore() {
        return BasicStorableFsm.builder()
                .id(id)
                .version(version)
                .state(state.restore())
                .transitions(transitions.stream()
                        .map(AbstractTransitionSnapshot::restore)
                        .collect(Collectors.toList()))
                .build();
    }

}
