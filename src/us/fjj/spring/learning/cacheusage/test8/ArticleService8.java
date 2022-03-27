package us.fjj.spring.learning.cacheusage.test8;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import java.util.HashMap;
import java.util.Map;

@CacheConfig(cacheNames = "cache1")
public class ArticleService8 {
    private Map<Long, String> articleMap = new HashMap<>();

    @Cacheable(key = "'findById'+#id")
    public String findById(Long id) {
        this.articleMap.put(1L, "spring系列");
        System.out.println("获取文章：" + id);
        return articleMap.get(id);
    }
}
