package us.fjj.spring.learning.cacheusage.test5;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ArticleService5 {
    Map<Long, String> articleMap = new HashMap<>();

    /**
     * 获取文章，先从缓存中获取，如果获取的结果为空，不要将结果放在缓存中
     * @param id
     * @return
     */
    @Cacheable(cacheNames = {"cache1"}, key = "'findById'+#id", unless = "#result==null")
    public String findById(Long id) {
        this.articleMap.put(1L, "hello world!");
        System.out.println("获取文章: "+ id);
        return articleMap.get(id);
    }
}
