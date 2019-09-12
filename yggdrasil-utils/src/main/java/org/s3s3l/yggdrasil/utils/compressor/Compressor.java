
package org.s3s3l.yggdrasil.utils.compressor;

import java.io.File;

import javax.annotation.Nonnull;

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

    byte[] compress(@Nonnull final byte[] in);
    
    void compress(@Nonnull final File in, final File out);

    byte[] decompress(@Nonnull final byte[] src);
    
    void decompress(@Nonnull final File in, final File out);

    Compressor config(Config config);
}
