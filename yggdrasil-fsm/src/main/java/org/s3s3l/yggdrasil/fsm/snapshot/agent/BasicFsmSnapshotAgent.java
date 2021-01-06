package org.s3s3l.yggdrasil.fsm.snapshot.agent;

import java.util.Optional;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import org.s3s3l.yggdrasil.bean.exception.VerifyException;
import org.s3s3l.yggdrasil.fsm.BasicStorableFsm;
import org.s3s3l.yggdrasil.fsm.exception.RestoreException;
import org.s3s3l.yggdrasil.fsm.exception.ShotingException;
import org.s3s3l.yggdrasil.fsm.snapshot.AbstractStateSnapshot;
import org.s3s3l.yggdrasil.fsm.snapshot.AbstractTransitionSnapshot;
import org.s3s3l.yggdrasil.fsm.snapshot.BasicFsmSnapshot;
import org.s3s3l.yggdrasil.fsm.state.State;
import org.s3s3l.yggdrasil.fsm.transition.Transition;
import org.s3s3l.yggdrasil.persistence.agent.ByteArraySnapshotAgent;
import org.s3s3l.yggdrasil.utils.collection.CollectionUtils;
import org.s3s3l.yggdrasil.utils.verify.Verify;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * <p>
 * </p>
 * Date: Sep 17, 2019 3:47:32 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Builder
@RequiredArgsConstructor
public class BasicFsmSnapshotAgent implements ByteArraySnapshotAgent<BasicStorableFsm, BasicFsmSnapshot> {
    private final ConcurrentMap<Class<? extends Transition>, ByteArraySnapshotAgent<Transition, AbstractTransitionSnapshot>> transitionSnapshotAgents;
    private final ByteArraySnapshotAgent<State, AbstractStateSnapshot> stateSnapshotAgent;

    public void addTransitionSnapshotAgent(Class<? extends Transition> transitionType,
            ByteArraySnapshotAgent<Transition, AbstractTransitionSnapshot> agent,
            boolean override) {
        Verify.notNull(transitionType);
        Verify.notNull(agent);
        if (override) {
            transitionSnapshotAgents.put(transitionType, agent);
        } else {
            transitionSnapshotAgents.putIfAbsent(transitionType, agent);
        }
    }

    @Override
    public BasicFsmSnapshot takeSnapshot(BasicStorableFsm data) {
        if (!testData(data)) {
            throw new ShotingException("Unavailable FSM data.");
        }
        return BasicFsmSnapshot.builder()
                .state(stateSnapshotAgent.takeSnapshot(data.getState()))
                .transitions(data.getTransitions()
                        .values()
                        .stream()
                        .map(transition -> transitionSnapshotAgents.get(transition.getClass())
                                .takeSnapshot(transition))
                        .collect(Collectors.toList()))
                .build()
                .id(data.getId())
                .version(data.getVersion());
    }

    @Override
    public BasicStorableFsm restore(BasicFsmSnapshot snapshot) {
        if (!testSnapshot(snapshot)) {
            throw new RestoreException("Unavailable snapshot.");
        }
        return snapshot.restore();
    }

    @Override
    public boolean testData(BasicStorableFsm data) {
        if (data == null) {
            return false;
        }

        Optional<Transition> firstOptional = CollectionUtils.getFirstOptional(data.getTransitions()
                .values(), transition -> !transitionSnapshotAgents.containsKey(transition.getClass()));
        if (firstOptional.isPresent()) {
            throw new VerifyException(String.format("No transition snapshot agent was found for type %s",
                    firstOptional.get()
                            .getClass()
                            .getName()));
        }

        return true;
    }

    @Override
    public boolean testSnapshot(BasicFsmSnapshot snapshot) {
        return snapshot != null;
    }

}
