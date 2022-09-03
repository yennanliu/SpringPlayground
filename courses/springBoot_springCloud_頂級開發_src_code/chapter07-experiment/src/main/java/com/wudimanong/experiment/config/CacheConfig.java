package com.wudimanong.experiment.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author jiangqiao
 */
//启用缓存
@EnableCaching
@Configuration
public class CacheConfig {

    /**
     * 缓存默认大小
     */
    public static final int DEFAULT_MAXSIZE = 50000;

    /**
     * 缓存默认过期时间(单位：秒)
     */
    public static final int DEFAULT_EXPIRE_TIME = 10;

    /**
     * 定义多种cache名称、超时时长（秒）、最大容量;需要修改可以在构造方法的参数中指定。
     */
    public enum Caches {
        //Caffeine缓存效果测试，缓存有效期5秒
        CAFFEINE_TEST(5, DEFAULT_MAXSIZE),

        //实验配置信息缓存，缓存有效期60秒
        EXP_CONFIG_INFO(60, DEFAULT_MAXSIZE);

        /**
         * 最大數量
         */
        private int maxSize = DEFAULT_MAXSIZE;

        /**
         * 过期时间（秒）
         */
        private int expireTime = DEFAULT_EXPIRE_TIME;


        /**
         * 缓存构造方法
         *
         * @param expireTime
         * @param maxSize
         */
        Caches(int expireTime, int maxSize) {
            this.expireTime = expireTime;
            this.maxSize = maxSize;
        }

        /**
         * 获取过期时间
         *
         * @return
         */
        int getExpireTime() {
            return this.expireTime;
        }

        /**
         * 获取缓存大小
         *
         * @return
         */
        int getMaxSize() {
            return this.maxSize;
        }
    }

    /**
     * 创建基于Caffeine的Cache Manager
     *
     * @return
     */
    @Bean
    @Primary
    public CacheManager caffeineCacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        //设置多种不同的缓存策略
        ArrayList<CaffeineCache> caches = new ArrayList<CaffeineCache>();
        for (Caches c : Caches.values()) {
            caches.add(new CaffeineCache(c.name(),
                    Caffeine.newBuilder().recordStats()
                            //在最后一次写入缓存后开始计时，在指定的时间后过期
                            .expireAfterWrite(c.getExpireTime(), TimeUnit.SECONDS)
                            //缓存最大容量大小
                            .maximumSize(c.getMaxSize())
                            .build())
            );
        }
        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
