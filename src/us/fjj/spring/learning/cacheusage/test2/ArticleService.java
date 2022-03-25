package us.fjj.spring.learning.cacheusage.test2;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ArticleService {
    @Cacheable(cacheNames = {"cache1"})
    public List<String> list() {
        System.out.println("获取文章列表");
        return Arrays.asList("spring", "mysql", "java高并发", "mybatis", "maven");
    }
}
