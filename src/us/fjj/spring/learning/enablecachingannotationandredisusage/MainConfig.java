package us.fjj.spring.learning.enablecachingannotationandredisusage;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@ComponentScan
@EnableCaching//@1
@Configuration
public class MainConfig {
    @Bean//@2
    public CacheManager cacheManager() throws IOException {
        RedissonSpringCacheManager cacheManager = new RedissonSpringCacheManager(redissonClient());
        cacheManager.setCacheNames(Arrays.asList("cache1"));
        return cacheManager;
    }

    @Bean//@3
    public RedissonClient redissonClient() throws IOException {
        InputStream is = MainConfig.class.getResourceAsStream("/us/fjj/spring/learning/enablecachingannotationandredisusage/redis.yml");
        Config config = Config.fromYAML(is);
        return Redisson.create(config);
    }

    /**
     * @1: 开启spring cache功能。
     * @2: 自定义spring中cache管理器，这个地方我们定义了一个redis类型的管理器，底层使用redis来作为缓存的存储介质。
     * @3: 通过redis.yml配置文件来创建一个RedissonClient，用于和redis进行交互。
     */






















}
