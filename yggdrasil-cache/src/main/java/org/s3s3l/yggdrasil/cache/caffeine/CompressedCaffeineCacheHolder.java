package org.s3s3l.yggdrasil.cache.caffeine;

import org.s3s3l.yggdrasil.cache.compressor.CompressConfiguration;
import org.s3s3l.yggdrasil.compress.Compressor;
import org.s3s3l.yggdrasil.utils.verify.Verify;

/**
 * <p>
 * </p>
 * ClassName:CompressedCaffeineCacheHolder <br>
 * Date: Apr 4, 2019 5:46:40 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class CompressedCaffeineCacheHolder extends CaffeineCacheHolder<byte[], byte[]> {

    protected boolean isCompressKey = false;
    protected boolean isCompressValue = false;
    protected Compressor keyCompressor;
    protected Compressor valueCompressor;

    public CompressedCaffeineCacheHolder(PartitionCaffeineCacheBuilder<byte[], byte[]> cacheBuilder) {
        super(cacheBuilder);
    }

    @Override
    protected byte[] compressKey(byte[] key) {
        return isCompressKey ? keyCompressor.compress(key) : key;
    }

    @Override
    protected byte[] compressValue(byte[] value) {
        return isCompressValue ? valueCompressor.compress(value) : value;
    }

    @Override
    protected byte[] decompressValue(byte[] value) {
        return isCompressValue ? valueCompressor.decompress(value) : value;
    }

    @Override
    public void configCompress(CompressConfiguration config) {
        Verify.notNull(config);
        Verify.notNull(config.getCompressorSupplier());
        this.isCompressKey = config.getProp()
                .isCompressKey();
        this.isCompressValue = config.getProp()
                .isCompressValue();
        if (config.getProp()
                .isCompressKey()) {
            Verify.notNull(config.getProp()
                    .getKeyConfig());
            this.keyCompressor = config.getCompressorSupplier()
                    .get()
                    .config(config.getProp()
                            .getKeyConfig());
        }
        if (config.getProp()
                .isCompressValue()) {
            Verify.notNull(config.getProp()
                    .getValueConfig());
            this.valueCompressor = config.getCompressorSupplier()
                    .get()
                    .config(config.getProp()
                            .getValueConfig());
        }
    }

}
