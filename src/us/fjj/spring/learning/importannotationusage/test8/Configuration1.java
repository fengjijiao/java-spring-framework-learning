package us.fjj.spring.learning.importannotationusage.test8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuration1 {
    @Bean
    public String name1() {
        System.out.println("name1");
        return "name1";
    }
}
