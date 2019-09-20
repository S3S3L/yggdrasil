package org.s3s3l.yggdrasil.compress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Deflater;
import java.util.zip.GZIPInputStream;

import org.s3s3l.yggdrasil.utils.file.FileUtils;

/**
 * <p>
 * </p>
 * ClassName:GzipCompressor <br>
 * Date: Apr 3, 2019 2:30:28 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class GzipCompressor implements LevelableCompressor {
    private Config config = new Config();

    public GzipCompressor() {
        this.config.setLevel(Deflater.DEFAULT_COMPRESSION);
    }

    @Override
    public byte[] compress(final byte[] in) {
        return compress(in, this.config.getLevel());
    }

    @Override
    public byte[] compress(final byte[] in, int level) {
        if (null == in) {
            throw new CompressException("Inputing bytes can not be null.");
        }

        Deflater def = new Deflater(level, true);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (ConfigurableGZIPOutputStream gzip = new ConfigurableGZIPOutputStream(def, out)) {
                gzip.write(in, 0, in.length);
            }
            return out.toByteArray();
        } catch (IOException oe) {
            throw new CompressException(oe);
        }
    }

    @Override
    public void compress(File in, File out) {
        compress(in, out, this.config.getLevel());
    }

    @Override
    public void compress(File in, File out, int level) {
        inputFileCheck(in);
        outputFileCheck(out);
        
        FileUtils.createMissingDirectories(out);
        Deflater def = new Deflater(level, true);

        try (InputStream is = new FileInputStream(in);
                OutputStream fos = new FileOutputStream(out, !this.config.isOverwrite());
                OutputStream os = new ConfigurableGZIPOutputStream(def, fos)) {
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            throw new CompressException(e);
        }

    }

    @Override
    public byte[] decompress(final byte[] src) {
        if (null == src) {
            if (this.config.isIgnoreNullValue()) {
                return null;
            }
            throw new DecompressException("Inputing bytes can not be null.");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                ByteArrayInputStream in = new ByteArrayInputStream(src);
                GZIPInputStream gzip = new GZIPInputStream(in)) {
            byte[] buf = new byte[128 * 1024];
            int n;
            while ((n = gzip.read(buf)) >= 0) {
                out.write(buf, 0, n);
            }

            return out.toByteArray();
        } catch (IOException e) {
            throw new DecompressException(e);
        }
    }

    @Override
    public void decompress(File in, File out) {
        inputFileCheck(in);
        outputFileCheck(out);
        
        FileUtils.createMissingDirectories(out);

        try (InputStream fis = new FileInputStream(in);
                InputStream is = new GZIPInputStream(fis);
                OutputStream os = new FileOutputStream(out, !this.config.isOverwrite())) {
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            throw new CompressException(e);
        }
    }

    @Override
    public Compressor config(Config config) {
        this.config = config;
        return this;
    }

    private void inputFileCheck(File in) {
        if (!in.exists()) {
            throw new CompressException(String.format("File not exists. %s", in.getAbsolutePath()));
        }
    }

    private void outputFileCheck(File out) {
        if (out.exists() && !this.config.isOverwrite()) {
            throw new CompressException(String.format("File already exists. %s", out.getAbsolutePath()));
        }
    }

}
