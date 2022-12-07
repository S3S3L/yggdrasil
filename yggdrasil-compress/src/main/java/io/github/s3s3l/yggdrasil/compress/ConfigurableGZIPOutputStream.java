package io.github.s3s3l.yggdrasil.compress;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 * <p>
 * </p>
 * ClassName:ConfigurableGZIPOutputStream <br>
 * Date: Apr 4, 2019 2:46:45 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ConfigurableGZIPOutputStream extends GZIPOutputStream {

    public ConfigurableGZIPOutputStream(Deflater def, OutputStream out, boolean syncFlush) throws IOException {
        super(out, syncFlush);
        this.def = def;
    }

    public ConfigurableGZIPOutputStream(Deflater def, OutputStream out, int size, boolean syncFlush)
            throws IOException {
        super(out, size, syncFlush);
        this.def = def;
    }

    public ConfigurableGZIPOutputStream(Deflater def, OutputStream out, int size) throws IOException {
        super(out, size);
        this.def = def;
    }

    public ConfigurableGZIPOutputStream(Deflater def, OutputStream out) throws IOException {
        super(out);
        this.def = def;
    }

}
