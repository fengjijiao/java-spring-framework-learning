package us.fjj.spring.learning.importannotationusage.test8;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configuration2 {
    @Bean
    public String name2() {
        System.out.println("name2");
        return "name2";
    }
}
