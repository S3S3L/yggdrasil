package org.s3s3l.yggdrasil.utils.cache.compressor;

import java.util.function.Supplier;

import org.s3s3l.yggdrasil.utils.compressor.Compressor;

import lombok.Data;

/**
 * <p>
 * </p>
 * ClassName:CompressConfiguration <br>
 * Date: Apr 3, 2019 7:26:16 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Data
public class CompressConfiguration {

    private CompressProperties prop;
    private Supplier<Compressor> compressorSupplier;
}
