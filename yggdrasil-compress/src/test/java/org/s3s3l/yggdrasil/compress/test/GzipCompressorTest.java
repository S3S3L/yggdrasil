package org.s3s3l.yggdrasil.compress.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.s3s3l.yggdrasil.compress.CompressException;
import org.s3s3l.yggdrasil.compress.DecompressException;
import org.s3s3l.yggdrasil.compress.GzipCompressor;
import org.s3s3l.yggdrasil.utils.file.FileUtils;

/**
 * <p>
 * </p>
 * ClassName:CompressorTest <br>
 * Date: Apr 3, 2019 2:45:19 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class GzipCompressorTest {
    private GzipCompressor gzip;
    private final byte[] src = "test".getBytes(StandardCharsets.UTF_8);
    private final String sourceFile = "compress/source.txt";
    private final String compressTargetFile = "compress/gzip/gzip.compress";
    private final String decompressTargetFile = "compress/gzip/gzip.decompress";

    @Before
    public void prepare() {
        gzip = new GzipCompressor();
    }

    @After
    public void clean() throws IOException {
        File compressTarget = FileUtils.getFirstExistFile(compressTargetFile);
        if (compressTarget != null) {
            Files.deleteIfExists(compressTarget.toPath());
        }
        File decompressTarget = FileUtils.getFirstExistFile(decompressTargetFile);
        if (decompressTarget != null) {
            Files.deleteIfExists(decompressTarget.toPath());
        }
    }

    @AfterClass
    public static void finalClent() throws IOException {
        File workFolder = FileUtils.getFirstExistFile("compress");
        if (workFolder != null && workFolder.exists()) {
            FileUtils.delete(workFolder);
        }
    }

    @Test
    public void gzipTest() {
        byte[] dis = gzip.compress(src);
        Assert.assertArrayEquals(gzip.decompress(dis), src);
    }

    @Test(expected = CompressException.class)
    public void gzipCompressExceptionTest() {
        gzip.compress(null);
    }

    @Test(expected = DecompressException.class)
    public void gzipDecompressExceptionTest() {
        gzip.decompress(null);
    }

    @Test
    public void gzipFileTest() throws IOException {
        gzip.compress(FileUtils.getFirstExistFile(sourceFile), new File(compressTargetFile));
        gzip.decompress(new File(compressTargetFile), new File(decompressTargetFile));

        Assert.assertEquals(FileUtils.readToEnd(FileUtils.getFirstExistResource(sourceFile)),
                FileUtils.readToEnd(FileUtils.getFirstExistResource(decompressTargetFile)));
    }
}
