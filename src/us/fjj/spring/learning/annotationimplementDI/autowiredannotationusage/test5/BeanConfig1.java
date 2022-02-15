package us.fjj.spring.learning.annotationimplementDI.autowiredannotationusage.test5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig1 {
    @Bean
    public String name() {
        return "OK";
    }
}
