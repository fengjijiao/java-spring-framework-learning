package us.fjj.spring.learning.aspectpointcutusage.test14;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy//important
public class BeanConfig1 {
    @Bean
    public Aspect14 aspect14() {
        return new Aspect14();
    }
    @Bean
    public BeanService beanService1() {
        return new BeanService("lxh yyds!");
    }
    @Bean
    public BeanService beanService2() {
        return new BeanService("jyx yyds!");
    }
}
