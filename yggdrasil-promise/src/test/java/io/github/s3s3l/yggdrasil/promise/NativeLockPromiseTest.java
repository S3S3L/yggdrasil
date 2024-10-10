package io.github.s3s3l.yggdrasil.promise;

import org.junit.jupiter.api.BeforeAll;

public class NativeLockPromiseTest extends BasePromiseTest {

    @BeforeAll
    static void beforeAll() {
        Promise.configure(PromiseImplements.NATIVE_LOCK);
    }
    
}
