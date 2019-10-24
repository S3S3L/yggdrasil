package org.s3s3l.yggdrasil.compress;

import java.io.File;

/**
 * <p>
 * </p>
 * ClassName:Compressor <br>
 * Date: Apr 3, 2019 1:54:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public interface Compressor {

    byte[] compress(final byte[] in);
    
    void compress(final File in, final File out);

    byte[] decompress(final byte[] src);
    
    void decompress(final File in, final File out);

    Compressor config(Config config);
}
