package us.fjj.spring.learning.cacheusage.test3;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class ArticleService2 {
    @Cacheable(cacheNames = {"cache1"}, key = "#root.target.class.name+'-'+#page+'-'+#pageSize")
    public String getPage(int page, int pageSize) {
        String msg = String.format("page-%s-pageSize-%s", page, pageSize);
        System.out.println("从db中获取数据："+msg);
        return msg;
    }
}
