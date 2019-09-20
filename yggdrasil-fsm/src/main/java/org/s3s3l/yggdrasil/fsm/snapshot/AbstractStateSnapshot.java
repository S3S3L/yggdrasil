package org.s3s3l.yggdrasil.fsm.snapshot;

import org.s3s3l.yggdrasil.fsm.state.State;
import org.s3s3l.yggdrasil.persistence.snapshot.ByteArraySnapshot;

/**
 * <p>
 * </p> 
 * Date:     Sep 16, 2019 4:03:51 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
public abstract class AbstractStateSnapshot extends VersioningSnapshot implements ByteArraySnapshot<State> {

}
  