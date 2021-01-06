package org.s3s3l.yggdrasil.compress;

import lombok.Data;

/**
 * <p>
 * </p> 
 * ClassName:Config <br> 
 * Date:     Apr 4, 2019 2:52:56 PM <br>
 *  
 * @author   kehw_zwei 
 * @version  1.0.0
 * @since    JDK 1.8
 */
@Data
public class Config {

    private int level;
    private boolean ignoreNullValue = false;
    private boolean overwrite = true;
}
  