package us.fjj.spring.learning.beanfactoryextensions.test4;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class MainConfig4 {
    @Bean
    public UserModel user1() {
        return new UserModel();
    }
    @Bean
    public UserModel user2() {
        return new UserModel();
    }
    @Bean
    public String name() {
        return "hello";
    }
}
