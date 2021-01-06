package org.s3s3l.yggdrasil.cache.compressor;

import org.s3s3l.yggdrasil.compress.Compressor;
import org.s3s3l.yggdrasil.utils.common.ObjectSerializer;

/**
 * <p>
 * </p>
 * ClassName:SerializableCompressor <br>
 * Date: Apr 4, 2019 2:21:44 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class SerializableCompressor implements ObjectCompressor<Object> {
    private final Compressor compressor;

    public SerializableCompressor(Compressor compressor) {
        this.compressor = compressor;
    }

    @Override
    public byte[] compress(Object obj) {
        return this.compressor.compress(ObjectSerializer.serialize(obj));
    }

    @Override
    public Object decompress(byte[] bytes) {
        return ObjectSerializer.desrialize(this.compressor.decompress(bytes));
    }

}
