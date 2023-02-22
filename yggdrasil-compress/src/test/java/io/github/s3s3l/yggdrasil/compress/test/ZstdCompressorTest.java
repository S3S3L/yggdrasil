package io.github.s3s3l.yggdrasil.compress.test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.s3s3l.yggdrasil.compress.CompressException;
import io.github.s3s3l.yggdrasil.compress.DecompressException;
import io.github.s3s3l.yggdrasil.compress.ZstdCompressor;
import io.github.s3s3l.yggdrasil.utils.file.FileUtils;

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

    @BeforeEach
    public void prepare() {
        zstd = new ZstdCompressor();
    }

    @AfterEach
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

    @AfterAll
    public static void finalClent() throws IOException {
        File workFolder = FileUtils.getFirstExistFile("compress");
        if (workFolder != null && workFolder.exists()) {
            FileUtils.delete(workFolder);
        }
    }

    @Test
    public void zstdTest() {
        byte[] dis = zstd.compress(src);
        Assertions.assertArrayEquals(zstd.decompress(dis), src);
    }

    @Test
    public void zstdLevelTest() {
        byte[] dis = zstd.compress(src, 5);
        Assertions.assertArrayEquals(zstd.decompress(dis), src);
    }

    @Test
    public void zstdFileTest() throws IOException {
        zstd.compress(FileUtils.getFirstExistFile(sourceFile), new File(compressTargetFile));
        zstd.decompress(new File(compressTargetFile), new File(decompressTargetFile));

        Assertions.assertEquals(FileUtils.readToEnd(FileUtils.getFirstExistResource(sourceFile)),
                FileUtils.readToEnd(FileUtils.getFirstExistResource(decompressTargetFile)));
    }

    @Test
    public void zstdCompressExceptionTest() {
        Assertions.assertThrows(CompressException.class, () -> zstd.compress(null));
    }

    @Test
    public void zstdLevelCompressExceptionTest() {
        Assertions.assertThrows(CompressException.class, () -> zstd.compress(null, 1));
    }

    @Test
    public void zstdDecompressExceptionTest() {
        Assertions.assertThrows(DecompressException.class, () -> zstd.decompress(null));
    }
}
