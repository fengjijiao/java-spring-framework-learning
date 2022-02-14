package us.fjj.spring.learning.importannotationusage.test5;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Module1Config {
    @Bean
    public String m1() {
        return "fjj";
    }

    @Bean
    public String m2() {
        return "wq";
    }
}
