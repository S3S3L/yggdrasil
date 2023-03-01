package test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.BeanUtils;

import io.github.s3s3l.yggdrasil.test.TimeCalExtension;
import io.github.s3s3l.yggdrasil.test.base.Timeout;
import io.github.s3s3l.yggdrasil.utils.reflect.ReflectionUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@ExtendWith(TimeCalExtension.class)
public class CopyPropertiesTest {
    private long loopTimes = 100_000L;

    @Data
    @SuperBuilder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Man {
        private String name;
        private int age;
        private boolean alive;
    }

    @BeforeAll
    public static void warmup() {
        Man man = Man.builder()
                .name("name")
                .age(18)
                .alive(true)
                .build();
        Man man2 = new Man();
        ReflectionUtils.copyProperties(man, man2);
        BeanUtils.copyProperties(man, man2);
        System.out.println("warmup finish");
    }

    @Timeout(200)
    @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)
    public void pdTest() throws InterruptedException {
        Man man = Man.builder()
                .name("name")
                .age(18)
                .alive(true)
                .build();
        Man man2 = new Man();

        for (long i = 0; i < loopTimes; i++) {
            ReflectionUtils.copyProperties(man, man2);
        }
    }

    @Timeout(1_000)
    @RepeatedTest(value = 10, name = RepeatedTest.LONG_DISPLAY_NAME)
    public void springBeanUtilsTest() {
        Man man = Man.builder()
                .name("name")
                .age(18)
                .alive(true)
                .build();
        Man man2 = new Man();

        for (long i = 0; i < loopTimes; i++) {
            BeanUtils.copyProperties(man, man2);
        }
    }
}
