package us.fjj.spring.learning.conditionalannotationusage.test8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig1 {
    @Bean
    public Service service() {
        return new Service();
    }
}
