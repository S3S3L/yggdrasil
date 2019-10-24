package org.s3s3l.yggdrasil.utils.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.s3s3l.yggdrasil.utils.file.FileUtils;

/**
 * <p>
 * </p>
 * Date: Aug 21, 2019 1:56:48 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class FileDeduplicateTest {

    private static final String WORK_SPACE = "file_test";
    private static final String SOURCE_DUPLICATE_FILE = WORK_SPACE.concat("/duplicate.src");
    private static final long SOURCE_FILE_LINE_NUM = 100000L;
    private static final int numRange = 100;

    @Before
    public void prepare() throws IOException {
        Random r = new Random();
        File sourceFile = new File(SOURCE_DUPLICATE_FILE);
        FileUtils.createMissingDirectories(sourceFile);
        if (sourceFile.exists()) {
            Files.delete(sourceFile.toPath());
        }

        try (OutputStream os = new FileOutputStream(sourceFile)) {
            for (int i = 0; i < SOURCE_FILE_LINE_NUM; i++) {
                os.write(String.valueOf(r.nextInt(numRange))
                        .concat(System.lineSeparator())
                        .getBytes(StandardCharsets.UTF_8));
            }
        }
    }

    @After
    public void clean() throws IOException {
        File sourceFile = new File(WORK_SPACE);
        FileUtils.delete(sourceFile);
    }

    @Test
    public void deduplicateSyncTest() throws IOException {
        File out = new File(WORK_SPACE.concat("/deduplicate.out"));
        FileUtils.deduplication(new File(SOURCE_DUPLICATE_FILE), out, WORK_SPACE, 16, 10240, 128, 10000, null);
        Assert.assertTrue(Files.lines(out.toPath())
                .count() <= numRange);
    }

    @Test
    public void deduplicateAsyncTest() throws IOException {
        File out = new File(WORK_SPACE.concat("/deduplicate.out"));
        FileUtils.deduplication(new File(SOURCE_DUPLICATE_FILE), out, WORK_SPACE, 16, 10240, 128, 10000,
                new ThreadPoolExecutor(16, 32, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024)));
        Assert.assertTrue(Files.lines(out.toPath())
                .count() <= numRange);
    }
}
