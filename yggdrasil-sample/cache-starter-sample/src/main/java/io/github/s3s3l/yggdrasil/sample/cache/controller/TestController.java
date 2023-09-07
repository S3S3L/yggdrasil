package io.github.s3s3l.yggdrasil.sample.cache.controller;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.s3s3l.yggdrasil.annotation.Cache;
import io.github.s3s3l.yggdrasil.annotation.CacheExpire;

@RequestMapping("test")
@RestController
public class TestController {
    private static final AtomicInteger COUNT = new AtomicInteger(0);

    /**
     * 添加到count作用域的缓存。缓存存在时，计数不会增长。
     * 
     * @return
     */
    @Cache("count")
    @GetMapping("getCountWithCache")
    public int getCountWithCache() {
        return getCount();
    }

    /**
     * 不使用缓存获取count计数
     * 
     * @return
     */
    @GetMapping("getCountNoCache")
    public int getCountNoCache() {
        return getCount();
    }

    /**
     * 使count作用域的缓存失效
     */
    @CacheExpire("count")
    @GetMapping("expireCache")
    public void expireCache() {
    }

    private int getCount() {
        return COUNT.incrementAndGet();
    }
}
