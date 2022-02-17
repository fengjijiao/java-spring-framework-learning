package us.fjj.spring.learning.internationalization.test3;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MainConfig3 {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource result = new ReloadableResourceBundleMessageSource();
        result.setBasenames("us/fjj/spring/learning/internationalization/test1/message");
        //设置缓存时间1000ms
        result.setCacheMillis(1000);
        return result;
    }
}
