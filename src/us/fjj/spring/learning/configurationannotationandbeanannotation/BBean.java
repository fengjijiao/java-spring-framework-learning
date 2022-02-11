package us.fjj.spring.learning.configurationannotationandbeanannotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 通过@Bean注解，向容器中注册了3个bean
 * serviceB1和serviceB2(@1和@2)都是通过this.serviceA()获取需要注入的ServiceA对象。
 * @0这行日志会输出几次呢？1 or 3
 */
@Configuration
public class BBean {
    @Bean
    public ServiceA serviceA() {
        System.out.println("调用了ServiceA()方法");//@0
        return new ServiceA();
    }
    @Bean
    public ServiceB serviceB1() {
        System.out.println("调用了ServiceB1()方法");
        ServiceA serviceA = this.serviceA();//@1
        return new ServiceB(serviceA);
    }
    @Bean
    public ServiceB serviceB2() {
        System.out.println("调用了ServiceB2()方法");//@2
        ServiceA serviceA = this.serviceA();
        return new ServiceB(serviceA);
    }
}
