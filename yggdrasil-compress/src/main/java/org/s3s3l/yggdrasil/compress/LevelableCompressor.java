package org.s3s3l.yggdrasil.compress;

import java.io.File;

/**
 * <p>
 * </p>
 * ClassName:LevelableCompressor <br>
 * Date: Apr 3, 2019 2:02:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface LevelableCompressor extends Compressor {

    byte[] compress(final byte[] in, int level);

    void compress(final File in, final File out, int level);
}
