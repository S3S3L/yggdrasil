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
import org.s3s3l.yggdrasil.compress.ZstdCompressor;
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
public class ZstdCompressorTest {
    private ZstdCompressor zstd;
    private final byte[] src = "test".getBytes(StandardCharsets.UTF_8);
    private final String sourceFile = "compress/source.txt";
    private final String compressTargetFile = "compress/zstd/sztd.compress";
    private final String decompressTargetFile = "compress/zstd/sztd.decompress";

    @Before
    public void prepare() {
        zstd = new ZstdCompressor();
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
    public void zstdTest() {
        byte[] dis = zstd.compress(src);
        Assert.assertArrayEquals(zstd.decompress(dis), src);
    }

    @Test
    public void zstdLevelTest() {
        byte[] dis = zstd.compress(src, 5);
        Assert.assertArrayEquals(zstd.decompress(dis), src);
    }

    @Test
    public void zstdFileTest() throws IOException {
        zstd.compress(FileUtils.getFirstExistFile(sourceFile), new File(compressTargetFile));
        zstd.decompress(new File(compressTargetFile), new File(decompressTargetFile));

        Assert.assertEquals(FileUtils.readToEnd(FileUtils.getFirstExistResource(sourceFile)),
                FileUtils.readToEnd(FileUtils.getFirstExistResource(decompressTargetFile)));
    }

    @Test(expected = CompressException.class)
    public void zstdCompressExceptionTest() {
        zstd.compress(null);
    }

    @Test(expected = CompressException.class)
    public void zstdLevelCompressExceptionTest() {
        zstd.compress(null, 1);
    }

    @Test(expected = DecompressException.class)
    public void zstdDecompressExceptionTest() {
        zstd.decompress(null);
    }
}
