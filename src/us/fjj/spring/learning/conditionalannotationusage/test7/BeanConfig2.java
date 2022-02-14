package us.fjj.spring.learning.conditionalannotationusage.test7;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig2 {
    @Bean
    public String name() {
        return "LAOKAI";
    }
}
