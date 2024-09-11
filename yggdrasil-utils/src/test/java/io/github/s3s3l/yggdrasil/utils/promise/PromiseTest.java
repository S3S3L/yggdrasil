package io.github.s3s3l.yggdrasil.utils.promise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

public class PromiseTest {

    @Data
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    static class TestObject {
        private String f1;
        private Integer f2;
        private Boolean f3;
    }

    @Test
    void allTest() throws InterruptedException, ExecutionException {
        List<TestObject> list = new ArrayList<>(4);
        Promise<Object[]> all = Promise.all(Promise.async(() -> {
            sleep(1000);
            TestObject testObject = TestObject.builder().f1("async1").f2(1).build();
            list.add(testObject);
            return testObject;
        }), Promise.async(() -> {
            sleep(2000);
            TestObject testObject = TestObject.builder().f1("async2").f2(2).build();
            list.add(testObject);
            return testObject;
        }), Promise.async(() -> {
            sleep(500);
            TestObject testObject = TestObject.builder().f1("async3").f2(3).build();
            list.add(testObject);
            return testObject;
        }), Promise.async(() -> {
            sleep(3000);
            TestObject testObject = TestObject.builder().f1("async4").f2(4).build();
            list.add(testObject);
            return testObject;
        }));

        Object[] res = all.get();

        Assertions.assertEquals(1, ((TestObject) res[0]).f2);
        Assertions.assertEquals(2, ((TestObject) res[1]).f2);
        Assertions.assertEquals(3, ((TestObject) res[2]).f2);
        Assertions.assertEquals(4, ((TestObject) res[3]).f2);

        Assertions.assertEquals(3, list.get(0).f2);
        Assertions.assertEquals(1, list.get(1).f2);
        Assertions.assertEquals(2, list.get(2).f2);
        Assertions.assertEquals(4, list.get(3).f2);
    }

    @Test
    void asyncTest() throws InterruptedException, ExecutionException {
        TestObject testObject = TestObject.builder().f1("async1").f2(1).build();
        Promise<TestObject> async = Promise.async(() -> {
            return testObject;
        }).then((res) -> {
            sleep(1000);
            res.setF2(2);
            return res;
        });
        Assertions.assertEquals(1, testObject.getF2());
        Assertions.assertEquals(2, async.get().getF2());
    }

    @Test
    void twoPromiseTest() throws InterruptedException, ExecutionException {
        List<TestObject> list = new ArrayList<>(2);
        Promise<Void> async1 = Promise.async(() -> {
            sleep(1000);
            return TestObject.builder().f1("async1").f2(1).build();
        }).then((res) -> {
            list.add(res);
        });
        Promise<Void> async2 = Promise.async(() -> {
            sleep(500);
            return TestObject.builder().f1("async2").f2(2).build();
        }).then((res) -> {
            list.add(res);
        });

        async1.get();
        async2.get();
        Assertions.assertEquals(1, list.get(1).getF2());
        Assertions.assertEquals(2, list.get(0).getF2());
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

}
