package io.github.s3s3l.yggdrasil.cache.compressor;

import java.util.function.Supplier;

import io.github.s3s3l.yggdrasil.compress.Compressor;

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
