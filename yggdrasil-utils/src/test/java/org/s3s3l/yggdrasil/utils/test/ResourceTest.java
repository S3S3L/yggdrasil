package org.s3s3l.yggdrasil.utils.test;

import java.io.IOException;

import org.junit.Test;
import org.s3s3l.yggdrasil.utils.file.FileUtils;

/**
 * <p>
 * </p>
 * ClassName:ResourceTest <br>
 * Date: Jan 26, 2018 6:39:32 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class ResourceTest {

    @Test
    public void resourceTest() throws IOException {
        FileUtils.readToEnd(FileUtils.getFirstExistResource("lua/sync_last_update.lua"));
    }
}
