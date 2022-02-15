package us.fjj.spring.learning.annotationimplementDI.other.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MainConfig1 {
    @Bean
    public Service1 service1() {
        return new Service1();
    }
//    @Bean
//    public Service2 service2() {
//        return new Service2();
//    }
    @Bean
    public Service3 service3(Service1 service1, @Autowired(required = false) Service2 service2) {
        Service3 service3 = new Service3();
        service3.setService1(service1);
        service3.setService2(service2);
        return service3;
    }
    /**
     * 方法有2个参数，第二个参数上标注了@Autowired(required=false)，说明第二个参数候选者不是必须的，找不到会注入一个null对象，第一个参数候选者是必须的，找不到会抛出异常
     *
     */
}
