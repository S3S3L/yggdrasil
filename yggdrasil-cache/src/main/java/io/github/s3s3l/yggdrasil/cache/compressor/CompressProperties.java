package io.github.s3s3l.yggdrasil.cache.compressor;

import io.github.s3s3l.yggdrasil.compress.Config;

import lombok.Data;
import lombok.ToString;

/**
 * <p>
 * </p>
 * ClassName:CompressProperties <br>
 * Date: Apr 10, 2019 8:47:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@ToString
@Data
public class CompressProperties {

    private boolean compressKey = false;
    private boolean compressValue = false;
    private Config keyConfig = new Config();
    private Config valueConfig = new Config();
}
