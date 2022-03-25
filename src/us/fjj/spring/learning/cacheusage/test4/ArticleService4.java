package us.fjj.spring.learning.cacheusage.test4;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ArticleService4 {
    @Cacheable(cacheNames = {"cache1"}, key = "'getByOne-'+#one", condition = "#cache")
    public List<String> getLxh(String one, boolean cache) {
        System.out.println("开始执行getLxh");
        return Arrays.asList(one, "lxh", "lxh!");
    }
}
