package us.fjj.spring.learning.lazyannotationusage.test2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Lazy
@Configuration
public class MainConfig2 {
    @Bean
    public Service1 service1() {
        return new Service1();
    }
    @Bean
    public Service2 service2() {
        return new Service2();
    }
    @Bean
    @Lazy(value = false)//通过覆盖，取消延迟加载
    public Service3 service3() {
        return new Service3();
    }
}
