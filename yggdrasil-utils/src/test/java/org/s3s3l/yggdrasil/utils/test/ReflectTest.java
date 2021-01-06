package org.s3s3l.yggdrasil.utils.test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.s3s3l.yggdrasil.utils.reflect.PropertyDescriptorReflectionBean;
import org.s3s3l.yggdrasil.utils.reflect.Reflection;
import org.s3s3l.yggdrasil.utils.reflect.ReflectionBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("deprecation")
public class ReflectTest {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Man {
        private String name;
        private int age;
        private boolean alive;
    }

    /**
     * 结论，Reflection构建更快，几乎可以不计时间，但是get/set平均比PropertyDescriptorReflectionBean慢80%。
     * 而PropertyDescriptorReflectionBean构建时间非常长，是Reflection的300000倍。
     * Reflection适用于频繁构建，少量操作场景。
     * PropertyDescriptorReflectionBean适用于少量构建，频繁操作场景。针对同一类型，仅第一次构建较慢，后续都是直接通过meta缓存直接构建。
     * 
     * @param args
     */
    public static void main(String[] args) {
        Man man = new Man("zwei", 28, true);

        int times = 10000000;

        LocalDateTime start;
        ReflectionBean rb = null;

        // Reflection
        System.out.println("Reflection");

        // create
        System.out.println("create");
        start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < times; i++) {
            rb = Reflection.create(man);
        }
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.NANOS) / times);

        // get
        System.out.println("get");
        start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < times; i++) {
            rb.getFieldValue("name");
        }
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.MILLIS));

        // set
        System.out.println("set");
        start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < times; i++) {
            rb.setFieldValue("name", "zwei");
        }
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.MILLIS));

        // PropertyDescriptorReflectionBean
        System.out.println("PropertyDescriptorReflectionBean");

        rb = new PropertyDescriptorReflectionBean(man);
        // create
        System.out.println("create");
        start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < 1000; i++) {
            rb = new PropertyDescriptorReflectionBean(man);
        }
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.NANOS) / 1000);

        // get
        System.out.println("get");
        start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < times; i++) {
            rb.getFieldValue("name");
        }
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.MILLIS));

        // set
        System.out.println("set");
        start = LocalDateTime.now();
        System.out.println(start);
        for (int i = 0; i < times; i++) {
            rb.setFieldValue("name", "zwei");
        }
        System.out.println(start.until(LocalDateTime.now(), ChronoUnit.MILLIS));

    }
}
