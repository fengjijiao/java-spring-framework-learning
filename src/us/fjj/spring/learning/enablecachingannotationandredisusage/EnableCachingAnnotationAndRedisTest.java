package us.fjj.spring.learning.enablecachingannotationandredisusage;

import org.junit.jupiter.api.Test;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 上篇文章主要介绍了spring中缓存的使用，不过文中的案例都是以本地内存作为存储介质的，但是实际上我们的项目上线之后，基本上都会采用集群的方式进行部署，如果将数据存储在本地内存中，集群之间无法共享，我们可以将数据存储在redis中，从而实现缓存的共享，下面我们一起来看下Spring中@EnableCaching如果对接redis。
 *
 *
 * 1.安装redis
 *
 * 2.pom.xml中引入redis配置
 * <!-- https://mvnrepository.com/artifact/org.redisson/redisson -->
 * <dependency>
 *     <groupId>org.redisson</groupId>
 *     <artifactId>redisson</artifactId>
 *     <version>3.17.0</version>
 * </dependency>
 *
 * springboot使用如下
 * <!-- https://mvnrepository.com/artifact/org.redisson/redisson-spring-boot-starter -->
 * <dependency>
 *     <groupId>org.redisson</groupId>
 *     <artifactId>redisson-spring-boot-starter</artifactId>
 *     <version>3.17.0</version>
 * </dependency>
 */
public class EnableCachingAnnotationAndRedisTest {
    /**
     * // TODO: 3/26/2022 存在问题
     * org.apache.logging.log4j.LoggingException: log4j-slf4j-impl cannot be present with log4j-to-slf4j
     */
    @Test
    public void test1() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        BookService bookService = context.getBean(BookService.class);
        System.out.println(bookService.list());
        System.out.println(bookService.list());
        {
            System.out.println("下面打印除cache1缓存中的key列表");
            RedissonSpringCacheManager cacheManager = context.getBean(RedissonSpringCacheManager.class);
            RedissonCache cache1 = (RedissonCache) cacheManager.getCache("cache1");
            cache1.getNativeCache().keySet().stream().forEach(System.out::println);
        }
    }
}
























































































