package us.fjj.spring.learning.internationalization.test1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MainConfig1 {
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource result = new ResourceBundleMessageSource();
        //可以指定国际化配置文件的位置，格式：路径/文件名称，注意不包含【语言_国家.properties】这部分
        result.setBasenames("us/fjj/spring/learning/internationalization/test1/message");
        return result;
    }
}
