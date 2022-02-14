package us.fjj.spring.learning.importannotationusage.test8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuration3 {
    @Bean
    public String name3() {
        System.out.println("name3");
        return "name3";
    }
}
