package io.github.s3s3l.yggdrasil.redis;

import java.time.LocalDateTime;

import io.github.s3s3l.yggdrasil.redis.base.IRedis;

/**
 * <p>
 * </p>
 * ClassName:AutoSwithRedis <br>
 * Date: Mar 10, 2018 2:32:09 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
public class AutoSwitchRedis {
    private LocalDateTime switchTime;
    private IRedis current;
    private IRedis next;

    public IRedis getCurrent() {
        return current;
    }

    public void setCurrent(IRedis current) {
        this.current = current;
    }

    public IRedis getNext() {
        return next;
    }

    public void setNext(IRedis next) {
        this.next = next;
    }

    public IRedis getResource() {
        return LocalDateTime.now()
                .isAfter(switchTime) ? next : current;
    }

    public LocalDateTime getSwitchTime() {
        return switchTime;
    }

    public void setSwitchTime(LocalDateTime switchTime) {
        this.switchTime = switchTime;
    }

}
