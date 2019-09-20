package org.s3s3l.yggdrasil.fsm.snapshot;

import org.s3s3l.yggdrasil.fsm.transition.Transition;
import org.s3s3l.yggdrasil.persistence.snapshot.ByteArraySnapshot;

/**
 * <p>
 * </p>
 * Date: Sep 16, 2019 4:22:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public abstract class AbstractTransitionSnapshot extends VersioningSnapshot implements ByteArraySnapshot<Transition> {
    
}
