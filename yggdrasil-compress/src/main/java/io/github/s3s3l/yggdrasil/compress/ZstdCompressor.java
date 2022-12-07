package io.github.s3s3l.yggdrasil.compress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import io.github.s3s3l.yggdrasil.utils.file.FileUtils;

import com.github.luben.zstd.Zstd;
import com.github.luben.zstd.ZstdInputStream;
import com.github.luben.zstd.ZstdOutputStream;

/**
 * <p>
 * </p>
 * ClassName:ZstdCompressor <br>
 * Date: Apr 3, 2019 2:01:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ZstdCompressor implements LevelableCompressor {
    private static final int MAX_DATA_SIZE = 2 * 1024 * 1024;
    private static final int DEFAULT_COMPRESS_LEVEL = 3;

    private Config config = new Config();

    public ZstdCompressor() {
        this.config.setLevel(DEFAULT_COMPRESS_LEVEL);
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
        return Zstd.compress(in, level);
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
        try (InputStream is = new FileInputStream(in);
                OutputStream fos = new FileOutputStream(out, !this.config.isOverwrite());
                OutputStream os = new ZstdOutputStream(fos, level)) {
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
                return new byte[] {};
            }
            throw new DecompressException("Inputing bytes can not be null.");
        }
        return Zstd.decompress(src, MAX_DATA_SIZE);
    }

    @Override
    public void decompress(File in, File out) {
        inputFileCheck(in);
        outputFileCheck(out);
        FileUtils.createMissingDirectories(out);
        try (InputStream fis = new FileInputStream(in);
                InputStream is = new ZstdInputStream(fis);
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
