package io.github.s3s3l.yggdrasil.cache.compressor;

import io.github.s3s3l.yggdrasil.compress.Compressor;

/**
 * <p>
 * </p>
 * ClassName:BufferCompressor <br>
 * Date: Apr 4, 2019 2:25:58 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class BufferCompressor implements ObjectCompressor<byte[]> {

    private final Compressor compressor;

    public BufferCompressor(Compressor compressor) {
        this.compressor = compressor;
    }

    @Override
    public byte[] compress(byte[] obj) {
        return compressor.compress(obj);
    }

    @Override
    public byte[] decompress(byte[] bytes) {
        return compressor.decompress(bytes);
    }

}
