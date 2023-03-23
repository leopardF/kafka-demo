package com.leopard.kafka.common;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 全局锁类
 *
 * @author leopard
 */
@Service
public class GlobalLock {

    private static final Logger logger = LoggerFactory.getLogger(GlobalLock.class);
    private final int tryTime = 60;
    private final int unLockTime = 40;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 是否释放锁 ，否则就加锁
     *
     * @param key
     * @param flag true：解锁，false:加锁
     * @return
     */
    public boolean releaseLock(String key, boolean flag) {

        RLock fairLock = redissonClient.getFairLock(key);
        boolean result = false;
        // 加锁
        if (!flag) {
            logger.debug("start Get lock ~");
            try {
                fairLock.lock(unLockTime, TimeUnit.SECONDS);
                result = fairLock.tryLock(tryTime, unLockTime, TimeUnit.SECONDS);
                if (result) {
                    logger.debug("get " + key + " success");
                } else {
                    logger.debug("get " + key + " fail");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                logger.error(e.getMessage());
            }
        }
        // 释放锁
        else {
            fairLock.forceUnlock();
            logger.debug("release " + key + " success");
            result = true;
        }

        return result;
    }
}
