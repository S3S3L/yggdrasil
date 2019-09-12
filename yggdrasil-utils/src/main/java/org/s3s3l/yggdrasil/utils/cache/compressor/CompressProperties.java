package org.s3s3l.yggdrasil.utils.cache.compressor;

import org.s3s3l.yggdrasil.utils.compressor.Config;

import lombok.Data;

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
@Data
public class CompressProperties {

    private boolean compressKey = false;
    private boolean compressValue = false;
    private Config keyConfig = new Config();
    private Config valueConfig = new Config();

    @Override
    public String toString() {
        return "CompressProperties [compressKey=" + compressKey + ", compressValue=" + compressValue + ", keyConfig="
                + keyConfig + ", valueConfig=" + valueConfig + "]";
    }
}
